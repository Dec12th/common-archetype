package com.benny.framework.common.framework.config.app;

import com.benny.framework.common.framework.service.template.CommonServiceExecuteTemplate;
import com.benny.framework.common.framework.service.template.ServiceExecuteTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 默认app配置
 * @author yin.beibei
 * @date 2018/11/22 15:42
 */
@Configuration
public class DefaultAppConfig {
    /**
     * 获取业务执行模板
     *
     * @return 通用业务执行模板
     */
    @Bean
    @ConditionalOnMissingBean(ServiceExecuteTemplate.class)
    public ServiceExecuteTemplate getServiceExecuteTemplate()
    {
        return new CommonServiceExecuteTemplate();
    }
}
