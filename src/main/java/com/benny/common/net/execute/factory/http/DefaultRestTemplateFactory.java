package com.benny.common.net.execute.factory.http;

import com.benny.common.net.domain.CommonNetConfig;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * 通用restTemplate工厂
 * @author yin.beibei
 * @date 2018/11/19 11:05
 */
public class DefaultRestTemplateFactory implements RestTemplateFactory {
    @Override
    public OkHttpClient createRestTemplate(CommonNetConfig commonNetConfig) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(commonNetConfig.getConnectionTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(commonNetConfig.getWriteTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(commonNetConfig.getReadTimeout(), TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(commonNetConfig.isRetryIfConnectFail())
                .connectionPool(new ConnectionPool(commonNetConfig.getConnectionPoolSize(), commonNetConfig.getConnectLifeExtension(), TimeUnit.MILLISECONDS))
                .build();

        return okHttpClient;
    }
}
