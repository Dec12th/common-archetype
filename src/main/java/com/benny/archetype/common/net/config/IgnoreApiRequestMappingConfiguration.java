package com.benny.archetype.common.net.config;

import com.benny.archetype.common.net.utils.IgnoredApiSupportRequestMappingHandlerMapping;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:14
 */
@Configuration
public class IgnoreApiRequestMappingConfiguration extends WebMvcRegistrationsAdapter {
    /**
     * @see WebMvcRegistrationsAdapter#getRequestMappingHandlerMapping()
     */
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping()
    {
        return new IgnoredApiSupportRequestMappingHandlerMapping();
    }
}
