package com.benny.common.net.config;

import com.benny.common.net.execute.factory.InterfaceProxyFactory;
import com.benny.common.net.execute.factory.http.SpringHttpInterfaceProxyFactory;
import com.netflix.discovery.EurekaClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 接口代理工厂配置
 * @author yin.beibei
 * @date 2018/11/19 12:29
 */
@Configuration
@Import(RestTemplateConfiguration.class)
public class InterfaceProxyFactoryConfiguration {
    /**
     * http客户端
     */
    @Autowired
    @Qualifier("common-net-http-client")
    private OkHttpClient httpClient;

    /**
     * 获取接口代理工厂
     *
     * @param eurekaClient 服務發現客戶端
     * @return 接口代理工厂
     */
    @Bean
    @Autowired
    public InterfaceProxyFactory getInterfaceProxyFactory(EurekaClient eurekaClient) {
        return new SpringHttpInterfaceProxyFactory(httpClient, eurekaClient);
    }
}
