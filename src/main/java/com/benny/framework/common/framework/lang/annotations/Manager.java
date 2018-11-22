package com.benny.framework.common.framework.lang.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 管理器注解
 * @author yin.beibei
 * @date 2018/11/22 14:35
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Manager {
    /**
     * Bean的名字
     * @return bean名
     */
    String value() default "";
}
