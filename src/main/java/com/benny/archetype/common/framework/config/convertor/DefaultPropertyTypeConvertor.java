package com.benny.archetype.common.framework.config.convertor;

/**
 * 默认参数结构类型转换器
 * @author yin.beibei
 * @date 2018/11/20 10:10
 */
public class DefaultPropertyTypeConvertor implements PropertyTypeConvertor<String> {
    /**
     * @see PropertyTypeConvertor#convert(String)
     */
    @Override
    public String convert(String propertyValueByStr)
    {
        return propertyValueByStr;
    }
}
