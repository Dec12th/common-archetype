package com.benny.common.net.config;

import com.benny.common.net.config.condition.EagerDisableConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 禁用饥饿加载配置
 * @author yin.beibei
 * @date 2018/11/19 14:04
 */
@Configuration
@AutoConfigureBefore(RibbonAutoConfiguration.class)
public class EagerDisableConfiguration {
    /**
     * 配置禁用饥饿加载配置
     * @return 禁用饥饿加载配置
     */
    @Bean
    @ConditionalOnProperty(value = "common.net.client.eager.disable", havingValue = "true")
    public EagerDisableConfig eagerDisableConfig()
    {
        return new EagerDisableConfig();
    }
}
