package com.benny.common.net.annotations;

import java.lang.annotation.*;

/**
 * <p>隐藏参数, 用于处理敏感参数在RPC日志中的表现形式</p>
 * <p>目前只能全部隐藏</p>
 * <p>有后续提到common包的打算</p>
 * @author yin.beibei
 * @date 2018/11/19 14:46
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HideArgument {
}
