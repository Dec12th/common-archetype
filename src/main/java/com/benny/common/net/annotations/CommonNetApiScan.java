package com.benny.common.net.annotations;

import com.benny.common.net.register.CommonNetApiRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 远程服务api扫描器
 * @author yin.beibei
 * @date 2018/11/19 14:05
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CommonNetApiRegister.class)
public @interface CommonNetApiScan {
    /**
     * 要扫描的包列表
     * @return 要扫描的包列表
     */
    String[] basePackages() default {};
}
