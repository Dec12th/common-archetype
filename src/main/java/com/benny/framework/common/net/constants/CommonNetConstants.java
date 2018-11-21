package com.benny.framework.common.net.constants;

import com.benny.framework.common.net.domain.CommonNetConfig;

/**
 * @author yin.beibei
 * @date 2018/11/19 12:37
 */
public class CommonNetConstants {
    /**
     * 信息编码
     */
    public static final String MESSAGE_CHARSET_NAME = "utf-8";

    /**
     * 默认从连接池获取连接的超时时间, 毫秒, {@link CommonNetConfig#connectionRequestTimeout}
     */
    public static final Integer DEFAULT_CONNECTION_REQUEST_TIMEOUT = 3000;

    /**
     * 默认客户端和服务端建立连接的超时时间, 毫秒, {@link CommonNetConfig#connectionTimeout}
     */
    public static final Integer DEFAULT_CONNECTION_TIMEOUT = 3000;

    /**
     * 默认客户端从服务端读取数据的超时时间, 毫秒, {@link CommonNetConfig#readTimeout}
     */
    public static final Integer DEFAULT_READ_TIMEOUT = 3000;

    /**
     * 默认客户端最大连接数, {@link CommonNetConfig#maxConnectCount}
     */
    public static final Integer DEFAULT_MAX_CONNECT = 400;

    /**
     * 默认客户端每台主机连接数, {@link CommonNetConfig#maxPerHostConnectCount}
     */
    public static final Integer DEFAULT_MAX_PER_HOST_CONNECT = 40;

    /**
     * 默认服务列表刷新间隔, 毫秒, {@link CommonNetConfig#serverListRefreshInterval}
     */
    public static final Long DEFAULT_SERVER_LIST_REFRESH_INTERVAL = 2000L;

    /**
     * 默认連接池大小, 毫秒, {@link CommonNetConfig#connectionPoolSize}
     */
    public static final Integer DEFAULT_CONNECTION_POOL_SIZE = 100;

    /**
     * 默认連接續命時間, 毫秒, {@link CommonNetConfig#connectLifeExtension}
     */
    public static final Integer DEFAULT_CONNECT_LIFE_EXTENSION = 30000;

    /**
     * 默认連接重試, {@link CommonNetConfig#isRetryIfConnectFail()}
     */
    public static final Boolean DEFAULT_CONNECT_FAIL_RETRY = true;
}
