package com.benny.framework.common.framework.lang.comand;

import java.util.function.Function;

/**
 * 命令执行者
 * @author yin.beibei
 * @date 2018/11/23 17:13
 */
@FunctionalInterface
public interface Executor<T,R> extends Function<T,R> {

}
