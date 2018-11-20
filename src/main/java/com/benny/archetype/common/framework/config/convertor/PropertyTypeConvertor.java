package com.benny.archetype.common.framework.config.convertor;

/**
 * 参数结构类型转换器
 * @author yin.beibei
 * @date 2018/11/20 9:46
 */
public interface PropertyTypeConvertor<T> {
    /**
     * 将字符串形式的配置值转换为特定结构的配置对象
     * @param propertyValueByStr 字符串形式的配置值
     * @return 特定结构的配置对象
     */
    T convert(String propertyValueByStr);
}
