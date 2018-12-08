package com.benny.framework.common.test;

import java.lang.annotation.*;

/**
 * @ClassName: MethodProxy
 * @Description:
 * @author:大贝
 * @date:2018年12月08日 19:42
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodProxy {
}
