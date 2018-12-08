package com.benny.framework.common.test;

import java.lang.annotation.*;

/**
 * @author yin.beibei
 * @date 2018/12/7 10:05
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomComponent {

    String value() default "";
}
