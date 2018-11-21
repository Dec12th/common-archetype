package com.benny.common.framework.service.template;

import com.alibaba.fastjson.JSON;
import com.benny.common.framework.lang.enums.CommonScenario;
import com.benny.common.framework.lang.exception.CommonErrorCode;
import com.benny.common.framework.lang.exception.CommonException;
import com.benny.common.framework.lang.exception.GenericException;
import com.benny.common.framework.util.ErrorContextBuilder;
import com.benny.common.framework.util.ScenarioHolder;
import com.benny.common.framework.util.ServiceObjectContainer;
import com.benny.common.net.domain.BaseResult;
import com.benny.common.net.domain.ErrorContext;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yin.beibei
 * @date 2018/11/19 17:40
 */
public class HystrixServiceExecuteTemplate implements ServiceExecuteTemplate {
    /**
     * 日志打印工具
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExecuteTemplate.class);

    /**
     * 应用名
     */
    private final String APP_NAME;

    /**
     * 是否支持降级
     */
    private final Boolean fallbackAble;

    /**
     * 最大并发数
     */
    private final Integer maxConcurrent;

    /**
     * hystrix服务的设定
     */
    private HystrixCommand.Setter setter;

    /**
     * 构造块
     */ {
        LOGGER.info("载入Hystrix业务执行模板实现类成功...");
    }

    /**
     * 构造函数
     *
     * @param appName 应用名
     */
    public HystrixServiceExecuteTemplate(String appName) {
        APP_NAME = appName;
        this.fallbackAble = true;
        this.maxConcurrent = 20;
        LOGGER.info("使用默认hystrix业务执行配置: {}", JSON.toJSONString(getSetter()));
    }

    /**
     * 构造函数
     *
     * @param appName 应用名
     * @param setter  hystrix配置
     */
    public HystrixServiceExecuteTemplate(String appName, HystrixCommand.Setter setter) {
        APP_NAME = appName;
        this.fallbackAble = true;
        this.maxConcurrent = 20;
        this.setter = setter;
        LOGGER.info("使用自定义hystrix业务执行配置: {}", JSON.toJSONString(getSetter()));
    }

    /**
     * 构造函数
     *
     * @param appName       应用名
     * @param fallbackAble  是否可降级
     * @param maxConcurrent 最大并发数
     */
    public HystrixServiceExecuteTemplate(String appName, boolean fallbackAble, Integer maxConcurrent) {
        APP_NAME = appName;
        this.fallbackAble = fallbackAble;
        this.maxConcurrent = maxConcurrent;
        LOGGER.info("使用默认hystrix业务执行配置: {}", JSON.toJSONString(getSetter()));
    }

    /**
     * @see ServiceExecuteTemplate#executeService(BaseResult, ServiceCallBack)
     */
    @Override
    public <T extends BaseResult> T executeService(T result, ServiceCallBack serviceCallBack) {
        // 初始化场景信息
        if (ScenarioHolder.get() == null) {
            ScenarioHolder.set(CommonScenario.COMMON);
        }

        new HystrixCommandExecutor(result, serviceCallBack).execute();

        return result;
    }

    /**
     * method for get setter
     */
    public HystrixCommand.Setter getSetter() {
        if (setter == null) {
            synchronized (this) {
                if (setter == null) {
                    setter = HystrixCommand.Setter

                            // 分组名
                            .withGroupKey(HystrixCommandGroupKey.Factory.asKey(APP_NAME))

                            // 配置
                            .andCommandPropertiesDefaults(
                                    HystrixCommandProperties.Setter()

                                                            // 是否可降级
                                                            .withFallbackEnabled(fallbackAble)

                                                            // 本地已做好线程隔离, 故使用信号量隔离
                                                            .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)

                                                            // 最大并发数
                                                            .withExecutionIsolationSemaphoreMaxConcurrentRequests(maxConcurrent)

                                                            // 降级策略时, 降级最大并发数
                                                            .withFallbackIsolationSemaphoreMaxConcurrentRequests(Runtime.getRuntime().availableProcessors() * 2 + 1)

                                                            // 关闭请求缓存
                                                            .withRequestCacheEnabled(false));
                }
            }
        }

        return setter;
    }

    /**
     * Hystrix业务执行器
     */
    private class HystrixCommandExecutor extends HystrixCommand<Void> {

        /**
         * 场景信息
         */
        private CommonScenario scenarioInfo;

        /**
         * 服务回调
         */
        private ServiceCallBack serviceCallBack;

        /**
         * 需要发送消息
         */
        private boolean needSendMessage;

        /**
         * 业务结果
         */
        private BaseResult result;

        /**
         * 构造函数
         *
         * @param baseResult      业务结果存储对象
         * @param serviceCallBack 业务回调
         */
        public HystrixCommandExecutor(BaseResult baseResult, ServiceCallBack serviceCallBack) {
            super(HystrixServiceExecuteTemplate.this.getSetter()

                                                    // 监控器名
                                                    .andCommandKey(HystrixCommandKey.Factory.asKey(((Enum) ScenarioHolder.get()).name()))
            );

            scenarioInfo = ScenarioHolder.get();
            this.result = baseResult;
            this.serviceCallBack = serviceCallBack;
        }

        /**
         * @see HystrixCommand#run()
         */
        @Override
        protected Void run() throws Exception {
            try {
                serviceCallBack.check();
                serviceCallBack.doService();
                if (result.isSuccess() && result.getErrorContext() == null) {
                    needSendMessage = true;
                }
            } catch (Exception e) {
                ErrorContext errorContext;

                // 如果是业务异常则不降级, 反之降级
                if (CommonException.class.isAssignableFrom(e.getClass())) {
                    errorContext = ErrorContextBuilder.
                                                              buildErrorContext(scenarioInfo, (CommonException) e);
                    result.setErrorContext(errorContext);
                } else {
                    errorContext = ErrorContextBuilder.
                                                              buildErrorContext(scenarioInfo,
                                                                      new GenericException(CommonErrorCode.UNKNOWN_ERROR));
                    result.setErrorContext(errorContext);

                    // 抛异常后进入降级逻辑
                    throw e;
                }
            } finally {
                if (needSendMessage) {
                    try {
                        serviceCallBack.afterService();
                    } catch (Exception e) {
                        LOGGER.error("业务后置逻辑执行异常", e);
                    }
                }

                // 清理容器
                ServiceObjectContainer.clear();

                // 清理业务信息
                ScenarioHolder.clear();
            }
            return null;
        }

        /**
         * @see HystrixCommand#run()
         */
        @Override
        protected Void getFallback() {
            // 如果回调可降级
            if (serviceCallBack instanceof FallbackableServiceCallBack) {
                try {
                    ((FallbackableServiceCallBack) serviceCallBack).fallback();
                } catch (Exception e) {
                    LOGGER.error("业务降级失败.", e);
                    throw new RuntimeException(e);
                }
            } else {
                // 不可降级则重新抛出
                throw new RuntimeException(getExecutionException());
            }

            return null;
        }
    }
}
