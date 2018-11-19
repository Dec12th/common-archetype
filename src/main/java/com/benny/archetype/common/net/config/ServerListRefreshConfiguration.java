package com.benny.archetype.common.net.config;

import com.benny.archetype.common.net.constants.CommonNetConstants;
import com.benny.archetype.common.net.domain.CommonNetConfig;
import com.netflix.loadbalancer.PollingServerListUpdater;
import com.netflix.loadbalancer.ServerListUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 服务列表刷新配置
 * @author yin.beibei
 * @date 2018/11/19 13:40
 */
@Configuration
@Import(CommonNetConfigConfiguration.class)
@AutoConfigureBefore(RibbonClientConfiguration.class)
public class ServerListRefreshConfiguration {
    /** 缓存更新延迟, 毫秒 */
    private static long SERVERS_CACHE_UPDATE_DELAY = 1000L;

    /**
     * 配置服务列表更新器
     * @return 服务列表更新器
     */
    @Bean
    @Autowired
    public ServerListUpdater ribbonServerListUpdater(CommonNetConfig commonNetConfig)
    {
        Long serverListRefreshInterval = commonNetConfig.getServerListRefreshInterval();

        if (serverListRefreshInterval == 0)
        {
            serverListRefreshInterval = CommonNetConstants.DEFAULT_SERVER_LIST_REFRESH_INTERVAL;
        }
        return new PollingServerListUpdater(SERVERS_CACHE_UPDATE_DELAY, serverListRefreshInterval);
    }
}
