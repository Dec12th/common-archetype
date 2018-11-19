package com.benny.archetype.common.net.execute.factory.http;

import com.benny.archetype.common.net.domain.CommonNetConfig;
import okhttp3.OkHttpClient;

import java.nio.charset.Charset;

/**
 * restTemplate工厂
 *
 * @author yin.beibei
 * @date 2018/11/19 10:57
 */
public interface RestTemplateFactory {
    /**
     * 默认编码
     */
    Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    /**
     * 创建restTemplate
     *
     * @param commonNetConfig 框架配置
     * @return restTemplate
     */
    OkHttpClient createRestTemplate(CommonNetConfig commonNetConfig);
}
