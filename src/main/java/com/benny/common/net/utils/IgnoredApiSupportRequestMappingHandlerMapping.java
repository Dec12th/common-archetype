package com.benny.common.net.utils;

import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 支持忽略作为api使用的requestMapping映射的映射处理器
 * @author yin.beibei
 * @date 2018/11/19 14:12
 */
public class IgnoredApiSupportRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    /**
     * @see RequestMappingHandlerMapping#isHandler(Class)
     */
    @Override
    protected boolean isHandler(Class<?> beanType)
    {
        if (super.isHandler(beanType))
        {
            for (String ignoredBasePackage : IgnoreApiRequestMappingContext.getAllIgnoredBasePackage())
            {
                if (beanType.getName().startsWith(ignoredBasePackage))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
