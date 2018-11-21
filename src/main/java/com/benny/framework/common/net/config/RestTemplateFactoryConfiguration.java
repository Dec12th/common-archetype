package com.benny.framework.common.net.config;

import com.benny.framework.common.net.execute.factory.http.DefaultRestTemplateFactory;
import com.benny.framework.common.net.execute.factory.http.RestTemplateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yin.beibei
 * @date 2018/11/19 12:34
 */
@Configuration
public class RestTemplateFactoryConfiguration {
    /**
     * 获取restTemplate工厂
     * @return restTemplate工厂
     */
    @Bean
    public RestTemplateFactory getRestTemplateFactory()
    {
        return new DefaultRestTemplateFactory();
    }
}
