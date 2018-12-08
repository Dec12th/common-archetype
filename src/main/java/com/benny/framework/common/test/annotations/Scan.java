package com.benny.framework.common.test.annotations;

import com.benny.framework.common.test.register.MethodProxyRegister;
import com.benny.framework.common.test.register.ScannerRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author yin.beibei
 * @date 2018/12/7 14:34
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ScannerRegister.class, MethodProxyRegister.class})
public @interface Scan {
    String[] basePackages() default {};
}
