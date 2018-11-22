package com.benny.framework.common.framework.lang.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 转换器注解
 * @author yin.beibei
 * @date 2018/11/22 14:34
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Convertor {
    /**
     * Bean的名字
     * @return bean名
     */
    String value() default "";
}
