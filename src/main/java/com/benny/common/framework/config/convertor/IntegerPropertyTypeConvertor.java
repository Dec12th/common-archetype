package com.benny.common.framework.config.convertor;

/**
 * @author yin.beibei
 * @date 2018/11/20 10:11
 */
public class IntegerPropertyTypeConvertor implements PropertyTypeConvertor<Integer> {
    /**
     * @see PropertyTypeConvertor#convert(String)
     */
    @Override
    public Integer convert(String propertyValueByStr)
    {
        if (propertyValueByStr == null)
        {
            return null;
        }
        return Integer.valueOf(propertyValueByStr);
    }
}
