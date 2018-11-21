package com.benny.common.net.config;

import ch.qos.logback.classic.LoggerContext;
import com.benny.common.net.log.factory.CommonNetLoggerFactory;
import com.benny.common.net.log.factory.GenericCommonNetLoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志配置, 目前仅支持logback日志框架
 *
 * @author yin.beibei
 * @date 2018/11/19 14:20
 */
@Configuration
@ConditionalOnClass(LoggerContext.class)
public class LogConfiguration {
    /**
     * 获取日志工厂
     *
     * @return 日志工厂
     */
    @Bean
    public CommonNetLoggerFactory getCommonNetLoggerFactory() {
        return new GenericCommonNetLoggerFactory();
    }

}
