package com.benny.framework.common.net.domain;

import lombok.Data;

/**
 * @author yin.beibei
 * @date 2018/11/19 11:01
 */
@Data
public class CommonNetConfig {
    /**
     * 从连接池获取连接的超时时间
     */
    private int connectionRequestTimeout;

    /**
     * 客户端和服务端建立连接的超时时间
     */
    private int connectionTimeout;

    /**
     * 客户端从服务端读取数据的超时时间
     */
    private int readTimeout;

    /**
     * 客户端从服务端写入数据的超时时间
     */
    private int writeTimeout;

    /**
     * 连接池最大连接数
     */
    private int maxConnectCount;

    /**
     * 每台主机最大连接数
     */
    private int maxPerHostConnectCount;

    /**
     * 服务列表刷新间隔, 毫秒
     */
    private long serverListRefreshInterval;

    /**
     * 连接池大小
     */
    private int connectionPoolSize;

    /**
     * 连接续命时间
     */
    private int connectLifeExtension;

    /**
     * 连接失败是否重试
     */
    private boolean retryIfConnectFail;
}
