package com.benny.framework.common.framework.service.template;

import com.benny.framework.common.framework.lang.enums.CommonScenario;
import com.benny.framework.common.framework.lang.exception.CommonErrorCode;
import com.benny.framework.common.framework.lang.exception.CommonException;
import com.benny.framework.common.framework.lang.exception.GenericException;
import com.benny.framework.common.framework.util.AssertUtil;
import com.benny.framework.common.framework.util.ErrorContextBuilder;
import com.benny.framework.common.framework.util.ScenarioHolder;
import com.benny.framework.common.framework.util.ServiceObjectContainer;
import com.benny.framework.common.net.domain.BaseResult;
import com.benny.framework.common.net.domain.ErrorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 业务执行模板实现类
 *
 * @author yin.beibei
 * @date 2018/11/20 10:16
 */
public class CommonServiceExecuteTemplate implements ServiceExecuteTemplate {

    /**
     * 日志打印工具
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExecuteTemplate.class);

    /**
     * 服务后逻辑执行器
     */
    private static final ExecutorService AFTER_SERVICE_EXEC = Executors.newFixedThreadPool(5);

    /**
     * 构造块
     */ {
        LOGGER.info("载入普通业务执行模板实现类成功...");
    }

    /**
     * @see ServiceExecuteTemplate#executeService(BaseResult, ServiceCallBack)
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseResult> T executeService(T result, ServiceCallBack serviceCallBack) {
        long current = System.currentTimeMillis();

        // 初始化场景信息
        if (ScenarioHolder.get() == null) {
            ScenarioHolder.set(CommonScenario.COMMON);
        }

        String executorId = UUID.randomUUID().toString();
        LOGGER.info("开始执行业务逻辑 executorId: [{}], serviceCallBack: [{}], scenarioCode: [{}]", executorId, serviceCallBack.getClass().getName(), ScenarioHolder.get());

        // 是否需要发送消息
        boolean needDoAfterService = false;

        try {
            serviceCallBack.check();

            serviceCallBack.doService();

            // 当业务执行成功才允许执行后置操作
            if (result.isSuccess()) {
                if (result.getErrorContext() == null) {
                    needDoAfterService = true;
                }
            } else {
                AssertUtil.isNotNull(result.getErrorContext(), CommonErrorCode.UNKNOWN_ERROR);
                AssertUtil.isNotBlank(result.getErrorContext().getErrorCode(), CommonErrorCode.UNKNOWN_ERROR);
            }
        } catch (Exception e) {
            ErrorContext errorContext;

            // 如果抛出了非业务异常
            if (CommonException.class.isAssignableFrom(e.getClass())) {
                LOGGER.warn("发生了业务异常", e);
                errorContext = ErrorContextBuilder.buildErrorContext(ScenarioHolder.get(), (CommonException) e);
            } else if (e instanceof IllegalArgumentException) {
                LOGGER.warn("捕获了了未包装的参数异常", e);
                errorContext = ErrorContextBuilder.buildErrorContext(ScenarioHolder.get(),
                                                                  new GenericException(CommonErrorCode.PARAM_ILLEGAL, e));
            } else {
                LOGGER.error("发生了非预期的异常", e);
                errorContext = ErrorContextBuilder.buildErrorContext(ScenarioHolder.get(),
                                                                  new GenericException(CommonErrorCode.UNKNOWN_ERROR, e));
            }

            result.setErrorContext(errorContext);
        } finally {

            if (needDoAfterService) {
                AFTER_SERVICE_EXEC.execute(() ->
                {
                    try {
                        serviceCallBack.afterService();
                    } catch (Exception e) {
                        LOGGER.error("执行后置逻辑时发生异常", e);
                    }
                });
            }

            LOGGER.info("业务逻辑执行完毕, executorId: [{}], serviceCallBack: [{}], scenarioCode: [{}], success: [{}], elapsedTime: [{}] ms", executorId, serviceCallBack.getClass().getName(), ScenarioHolder.get(), result.isSuccess(), System.currentTimeMillis() - current);

            // 清理容器
            ServiceObjectContainer.clear();

            // 清理业务信息
            ScenarioHolder.clear();
        }

        return result;
    }
}
