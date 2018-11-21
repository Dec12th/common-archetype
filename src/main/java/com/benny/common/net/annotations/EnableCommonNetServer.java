package com.benny.common.net.annotations;

import com.benny.common.net.config.HttpMessageConvertersConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 作为一个RPC服务提供方
 * @author yin.beibei
 * @date 2018/11/19 14:51
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(HttpMessageConvertersConfiguration.class)
public @interface EnableCommonNetServer {
}
