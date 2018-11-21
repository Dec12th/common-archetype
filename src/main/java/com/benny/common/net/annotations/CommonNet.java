package com.benny.common.net.annotations;

import java.lang.annotation.*;

/**
 * 远程服务
 * @author yin.beibei
 * @date 2018/11/19 12:00
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CommonNet {
    /**
     * 远程服务应用名
     * @return 远程服务应用名
     */
    String appName();
}
