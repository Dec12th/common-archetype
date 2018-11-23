package com.benny.framework.common.framework.lang.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 策略注解
 * @author yin.beibei
 * @date 2018/11/23 15:26
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Strategy {
    /**
     * Bean的名字
     * @return bean名
     */
    String value() default "";
}
