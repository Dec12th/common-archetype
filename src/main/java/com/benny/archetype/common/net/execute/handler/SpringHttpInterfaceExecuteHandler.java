package com.benny.archetype.common.net.execute.handler;

import com.benny.archetype.common.net.domain.BaseRequest;
import com.benny.archetype.common.net.domain.BaseResult;
import com.benny.archetype.common.net.domain.InterfaceInfo;
import com.benny.archetype.common.net.domain.ServicePointInfo;
import com.benny.archetype.common.net.exception.InterfaceInfoInValidException;
import com.benny.archetype.common.net.log.CommonNetLogger;
import com.benny.archetype.common.net.log.LoggerTypeEnum;
import com.benny.archetype.common.net.log.factory.CommonNetLoggerFactory;
import com.benny.archetype.common.net.log.factory.GenericCommonNetLoggerFactory;
import com.netflix.discovery.EurekaClient;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author yin.beibei
 * @date 2018/11/19 11:21
 */
public class SpringHttpInterfaceExecuteHandler implements ExecuteHandler {

   /** 日志工厂 */
    private static final CommonNetLoggerFactory<Class> LOGGER_FACTORY = new GenericCommonNetLoggerFactory();

    /** 日志记录器 */
    private final CommonNetLogger logger;

    /**
     * 方法-接口信息映射
     */
    private Map<Method, ServicePointInfo> methodInterfaceInfoMap;

    /**
     * 调用点信息映射
     */
    private Map<ServicePointInfo, String> invokePointMap;

    /**
     * 接口信息
     */
    private InterfaceInfo interfaceInfo;

    /**
     * 服務發現客戶端
     */
    private EurekaClient eurekaClient;

    /**
     * okHttp客戶端
     */
    private OkHttpClient okHttpClient;

    /**
     * 内容類型
     */
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    /**
     * 内容類型key
     */
    private static final String CONTENT_TYPE_KEY = "Content-Type";

    /**
     * 接受的編碼
     */
    private static final String ACCEPT_CHARSET_KEY = "Accept-Charset";

    /**
     * 接受的編碼
     */
    private static final Charset ACCEPT_CHARSET = StandardCharsets.UTF_8;

    /**
     * json類型
     */
    private static final MediaType JSON_TYPE = MediaType.parse(CONTENT_TYPE);

    /**
     * 回復成功碼
     */
    private static final Integer RESPONSE_OK_CODE = 200;

    /**
     * 隨機發生器
     */
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    /**
     * 构造函数
     *
     * @param interfaceInfo 接口信息
     * @param okHttpClient  okhttp客戶端
     * @param eurekaClient  服務發現客戶端
     */
    @SuppressWarnings("unchecked")
    public SpringHttpInterfaceExecuteHandler(InterfaceInfo interfaceInfo, OkHttpClient okHttpClient, EurekaClient eurekaClient) {
        checkInterfaceInfoValid(interfaceInfo);


        logger = LOGGER_FACTORY.getLogger(interfaceInfo.getInterfaceClass(), LoggerTypeEnum.CLIENT);

        this.interfaceInfo = interfaceInfo;
        this.okHttpClient = okHttpClient;
        this.eurekaClient = eurekaClient;
        this.methodInterfaceInfoMap = convertToClientPointInterfaceInfoMap(this.interfaceInfo.getServicePointInfos());
        this.invokePointMap = new HashMap<>();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }

    /**
     * 检查接口信息是否合法
     * @param interfaceInfo 接口信息
     */
    private void checkInterfaceInfoValid(InterfaceInfo interfaceInfo)
    {
        checkObjectNotEmpty(interfaceInfo);
        checkObjectNotEmpty(interfaceInfo.getAppName());
        checkObjectNotEmpty(interfaceInfo.getInterfaceClass());

        // 接口可以没有方法, 但是如果有方法必定要有足够且合法的信息
        if (!CollectionUtils.isEmpty(interfaceInfo.getServicePointInfos()))
        {
            checkServicePointInfosValid(interfaceInfo.getServicePointInfos());
        }
    }

    /**
     * 检查对象不为null或空值
     * @param object 对象
     */
    private void checkObjectNotEmpty(Object object)
    {
        if (object == null)
        {
            throw new InterfaceInfoInValidException();
        }

        if (object instanceof String)
        {
            if (StringUtils.isBlank((String) object))
            {
                throw new InterfaceInfoInValidException();
            }
        }
    }

    /**
     * 检查服务点信息列表是否合法
     * @param servicePointInfos 服务点信息列表
     */
    private void checkServicePointInfosValid(List<ServicePointInfo> servicePointInfos)
    {
        if (CollectionUtils.isEmpty(servicePointInfos))
        {
            return;
        }

        Class interfaceClass = null;
        for (ServicePointInfo servicePointInfo : servicePointInfos)
        {
            checkObjectNotEmpty(servicePointInfo);
            checkObjectNotEmpty(servicePointInfo.getInterfaceClass());
            checkObjectNotEmpty(servicePointInfo.getResultType());
            checkObjectNotEmpty(servicePointInfo.getServiceClientPoint());
            checkObjectNotEmpty(servicePointInfo.getServiceServerPoint());
            checkObjectNotEmpty(servicePointInfo.getArgumentType());

            if (!BaseRequest.class.isAssignableFrom(servicePointInfo.getArgumentType()))
            {
                throw new InterfaceInfoInValidException();
            }

            if (!BaseResult.class.isAssignableFrom(servicePointInfo.getResultType()))
            {
                throw new InterfaceInfoInValidException();
            }

            if (interfaceClass == null)
            {
                interfaceClass = servicePointInfo.getInterfaceClass();
            }
            else
            {
                if (interfaceClass != servicePointInfo.getInterfaceClass())
                {
                    throw new InterfaceInfoInValidException();
                }
            }
        }
    }

    /**
     * 将接口信息列表转换为客户端服务点接口信息映射
     * @param servicePointInfos 接口信息列表
     * @return 客户端服务点接口信息映射
     */
    private Map<Method, ServicePointInfo> convertToClientPointInterfaceInfoMap(List<ServicePointInfo> servicePointInfos)
    {
        if (CollectionUtils.isEmpty(servicePointInfos))
        {
            return null;
        }

        Map<Method, ServicePointInfo> methodInterfaceInfoMap = new HashMap<>();
        for (ServicePointInfo servicePointInfo : servicePointInfos)
        {
            methodInterfaceInfoMap.put(servicePointInfo.getServiceClientPoint(), servicePointInfo);
        }

        return Collections.unmodifiableMap(methodInterfaceInfoMap);
    }
}
