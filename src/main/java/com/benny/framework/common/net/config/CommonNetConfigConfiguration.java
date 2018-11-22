package com.benny.framework.common.net.config;

import com.benny.framework.common.net.constants.CommonNetConstants;
import com.benny.framework.common.net.domain.CommonNetConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yin.beibei
 * @date 2018/11/19 12:36
 */
@Configuration
public class CommonNetConfigConfiguration {
    /**
     * 获取框架默认配置
     * @return 框架默认配置
     */
    @Bean
    @ConditionalOnMissingBean(CommonNetConfig.class)
    public CommonNetConfig getCommonNetConfig()
    {
        CommonNetConfig commonNetConfig = new CommonNetConfig();
        commonNetConfig.setConnectionRequestTimeout(CommonNetConstants.DEFAULT_CONNECTION_REQUEST_TIMEOUT);
        commonNetConfig.setConnectionTimeout(CommonNetConstants.DEFAULT_CONNECTION_TIMEOUT);
        commonNetConfig.setReadTimeout(CommonNetConstants.DEFAULT_READ_TIMEOUT);
        commonNetConfig.setMaxConnectCount(CommonNetConstants.DEFAULT_MAX_CONNECT);
        commonNetConfig.setMaxPerHostConnectCount(CommonNetConstants.DEFAULT_MAX_PER_HOST_CONNECT);
        commonNetConfig.setServerListRefreshInterval(CommonNetConstants.DEFAULT_SERVER_LIST_REFRESH_INTERVAL);
        commonNetConfig.setRetryIfConnectFail(CommonNetConstants.DEFAULT_CONNECT_FAIL_RETRY);
        commonNetConfig.setConnectionPoolSize(CommonNetConstants.DEFAULT_CONNECTION_POOL_SIZE);
        commonNetConfig.setConnectLifeExtension(CommonNetConstants.DEFAULT_CONNECT_LIFE_EXTENSION);

        return commonNetConfig;
    }
}
