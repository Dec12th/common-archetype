package com.benny.common.net.config;

import com.benny.common.net.config.condition.EagerDisableConfig;
import com.benny.common.net.utils.RemoteAppsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.RibbonApplicationContextInitializer;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ribbon饥饿加载配置
 * @author yin.beibei
 * @date 2018/11/19 14:02
 */
@Configuration
@AutoConfigureAfter({RibbonAutoConfiguration.class})
public class RibbonEagerConfiguration {
    /**
     * 配置ribbon上下文初始化器
     * @param springClientFactory spring客户端工厂
     * @return ribbon上下文初始化器
     */
    @Bean
    @Autowired
    @ConditionalOnMissingBean(EagerDisableConfig.class)
    public RibbonApplicationContextInitializer ribbonApplicationContextInitializer(SpringClientFactory springClientFactory)
    {
        return new RibbonApplicationContextInitializer(springClientFactory,
                RemoteAppsContext.getAllRemoteAppList());
    }
}
