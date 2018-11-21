package com.benny.common.net.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.benny.common.net.annotations.HideArgument;
import com.benny.common.net.domain.BaseRequest;
import com.benny.common.net.domain.BaseResult;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:45
 */
public class GenericCommonNetLogger implements CommonNetLogger {
    /** 日志记录器, 实际上日志记录都是交给这个对象 */
    private final Logger logger;

    /** 参数过滤器 */
    private static final PropertyPreFilter PROPERTY_PRE_FILTER = new GenericCommonNetLoggerFilter();

    /** 方法调用时间映射, key -> traceId, value -> invokeTime */
    private static final Map<String, Long> INVOKE_TIME_MAP = new ConcurrentHashMap<>();

    /**
     * 构造函数, 载入日志记录器
     * @param logger 日志记录器
     */
    public GenericCommonNetLogger(Logger logger)
    {
        this.logger = logger;
    }

    /**
     * @see CommonNetLogger#logRequest(Object, BaseRequest)
     */
    @Override
    public void logRequest(Object invokePoint, BaseRequest request)
    {
        String requestBody = JSON.toJSONString(request, PROPERTY_PRE_FILTER);
        info("发出请求: #{}# - {}", invokePoint.toString(), requestBody);
    }

    /**
     * @see CommonNetLogger#logResult(Object, BaseResult)
     */
    @Override
    public void logResult(Object invokePoint, BaseResult result)
    {
        String resultBody = JSON.toJSONString(result, PROPERTY_PRE_FILTER);
        info("收到结果: #{}# - {}", invokePoint.toString(), resultBody);
    }

    /**
     * @see CommonNetLogger#info(String)
     */
    @Override
    public void info(String message)
    {
        logger.info(message);
    }

    /**
     * @see CommonNetLogger#info(String, Object...)
     */
    @Override
    public void info(String format, Object... message)
    {
        logger.info(format, message);
    }

    /**
     * @see CommonNetLogger#warn(String)
     */
    @Override
    public void warn(String message)
    {
        logger.warn(message);
    }

    /**
     * @see CommonNetLogger#warn(String, Object...)
     */
    @Override
    public void warn(String format, Object... message)
    {
        logger.warn(format, message);
    }

    /**
     * @see CommonNetLogger#error(String)
     */
    @Override
    public void error(String message)
    {
        logger.error(message);
    }

    /**
     * @see CommonNetLogger#error(String, Object...)
     */
    @Override
    public void error(String format, Object... message)
    {
        logger.error(format, message);
    }

    /**
     * @see CommonNetLogger#error(String, Throwable)
     */
    @Override
    public void error(String message, Throwable throwable)
    {
        logger.error(message, throwable);
    }

}

/**
 * 通用日志过滤器, 过滤敏感信息
 */
class GenericCommonNetLoggerFilter implements PropertyPreFilter
{
    /** 类与排除序列化的字段名的映射 */
    private static final Map<Class, Set<String>> CLASS_EXCLUDES_MAP = new ConcurrentHashMap<>();

    /**
     * @see PropertyPreFilter#apply(JSONSerializer, Object, String)
     */
    @Override
    public boolean apply(JSONSerializer serializer, Object object, String name)
    {
        final Class objectClass = object.getClass();
        Set<String> excludesFieldsNames = CLASS_EXCLUDES_MAP.get(objectClass);
        if (excludesFieldsNames == null)
        {
            synchronized (objectClass)
            {
                if (CLASS_EXCLUDES_MAP.get(objectClass) == null)
                {
                    resolverClassToExcludesMap(objectClass);
                }
            }
            return apply(serializer, object, name);
        }
        else
        {
            return !excludesFieldsNames.contains(name);
        }
    }

    /**
     * 将类解析至<code>类与排除序列化的字段名的映射</code>
     * @param objectClass 类
     */
    private void resolverClassToExcludesMap(Class objectClass)
    {
        Set<String> excludesFieldsNames;
        excludesFieldsNames = new HashSet<>();
        Field[] fields = objectClass.getDeclaredFields();
        for (Field field : fields)
        {
            if (field.isAnnotationPresent(HideArgument.class))
            {
                excludesFieldsNames.add(field.getName());
            }
        }

        CLASS_EXCLUDES_MAP.put(objectClass, excludesFieldsNames);
    }
}
