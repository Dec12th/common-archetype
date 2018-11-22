package com.benny.framework.common.net.annotations;


import com.benny.framework.common.net.config.*;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>启用common-net客户端框架, 需要搭配api扫描器一起使用, {@link CommonNetApiScan}</p>
 * @author yin.beibei
 * @date 2018/11/19 14:13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({InterfaceProxyFactoryConfiguration.class,
        RibbonEagerConfiguration.class,
        EagerDisableConfiguration.class,
        ServerListRefreshConfiguration.class,
        LogConfiguration.class,
        IgnoreApiRequestMappingConfiguration.class})
public @interface EnableCommonNetClient {
}
