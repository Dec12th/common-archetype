package com.benny.framework.common.net.config;

import com.benny.framework.common.net.domain.CommonNetConfig;
import com.benny.framework.common.net.execute.factory.http.RestTemplateFactory;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author yin.beibei
 * @date 2018/11/19 12:30
 */
@Configuration
@Import({RestTemplateFactoryConfiguration.class, CommonNetConfigConfiguration.class})
public class RestTemplateConfiguration {
    /** 框架配置 */
    @Autowired
    private CommonNetConfig commonNetConfig;

    /** restTemplate工厂 */
    @Autowired
    private RestTemplateFactory restTemplateFactory;


    /**
     * 获取拥有负载均衡功能的http客户端
     * @return 拥有负载均衡功能的http客户端
     */
    @Bean(name = "common-net-http-client")
    @LoadBalanced
    public OkHttpClient getHttpClient()
    {
        return restTemplateFactory.createRestTemplate(commonNetConfig);
    }
}
