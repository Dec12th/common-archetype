package com.benny.framework.common.net.config;

import com.benny.framework.common.net.utils.HttpMessageConvertorsContainer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:51
 */
@Configuration
public class HttpMessageConvertersConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
    {
        converters.clear();
        converters.addAll(HttpMessageConvertorsContainer.getDefinedConvertors());
    }
}
