package com.benny.archetype.common.framework.config;

import com.benny.archetype.common.framework.config.convertor.DefaultPropertyTypeConvertor;
import com.benny.archetype.common.framework.config.convertor.IntegerPropertyTypeConvertor;
import com.benny.archetype.common.framework.config.convertor.PropertyTypeConvertor;
import com.benny.archetype.common.framework.config.ex.PropertyNotExistsException;
import com.benny.archetype.common.framework.config.ex.PropertyTypeConvertorNotExistsException;
import com.benny.archetype.common.framework.lang.exception.GenericException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>通用配置, 可以配置这些项目 {@link ConfigKeyEnum}</p>
 * <p>该配置不可动态变更</p>
 * @author yin.beibei
 * @date 2018/11/20 9:45
 */
public class CommonConfig {
    /** 日志记录器 */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConfig.class);

    /** 属性源 */
    private static final Properties PROPERTIES_SOURCE = new Properties();

    /** 配置文件路径 */
    private static final String CONFIG_PATH = "config/common.properties";

    /** 配置结构类型 */
    private static final Map<Class, PropertyTypeConvertor> PROPERTY_TYPE_CONVERTOR_MAP = new ConcurrentHashMap<>();


    // 初始化配置源
    static
    {
        loadProperties();

        loadPropertyTypeConvertor();
    }


    /**
     * 获取对应key的配置信息
     * @param configKeyEnum 配置枚举
     * @return 配置信息
     */
    public static <T> T getProperty(ConfigKeyEnum configKeyEnum) throws GenericException
    {
        String value = PROPERTIES_SOURCE.getProperty(configKeyEnum.getCode());
        if (!configKeyEnum.isAllowNull() && value == null)
        {
            LOGGER.error("{} 配置不可为空, 请配置之.", configKeyEnum.getCode());
            throw new PropertyNotExistsException(configKeyEnum);
        }

        return convert(value, configKeyEnum.getPropertyType());
    }

    /**
     * 将字符串形式的配置值转换为对应的配置结构对象
     * @param value 字符串形式的配置值
     * @param clazz 对应的配置结构对象类型
     * @param <T>   对应的配置结构对象类型
     * @return 对应的配置结构对象
     */
    private static <T> T convert(String value, Class<T> clazz)
    {
        PropertyTypeConvertor<T> propertyTypeConvertor = getPropertyTypeConvertor(clazz);
        if (propertyTypeConvertor == null)
        {
            throw new PropertyTypeConvertorNotExistsException(clazz);
        }

        return propertyTypeConvertor.convert(value);
    }


    /**
     * 获取相应结构类型的转换器
     * @param clazz 类型
     * @return 相应结构类型的转换器
     */
    @SuppressWarnings("unchecked")
    private static <T> PropertyTypeConvertor<T> getPropertyTypeConvertor(Class<T> clazz)
    {
        return PROPERTY_TYPE_CONVERTOR_MAP.get(clazz);
    }

    /**
     * 载入配置结构转换器
     */
    private static void loadPropertyTypeConvertor()
    {
        PROPERTY_TYPE_CONVERTOR_MAP.put(String.class, new DefaultPropertyTypeConvertor());
        PROPERTY_TYPE_CONVERTOR_MAP.put(Integer.class, new IntegerPropertyTypeConvertor());
    }

    /**
     * 载入配置信息
     */
    private static void loadProperties()
    {
        try
        {
            LOGGER.info("开始载入配置文件: {}", CONFIG_PATH);
            PROPERTIES_SOURCE.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_PATH));
            LOGGER.info("载入配置文件: {} 成功", CONFIG_PATH);
        }
        catch (Exception e)
        {
            LOGGER.error("载入配置文件: {} 失败!", CONFIG_PATH);
        }
    }

}
