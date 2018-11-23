package com.benny.framework.common.framework.lang.comand;

import com.benny.framework.common.framework.lang.Type;
import com.benny.framework.common.framework.lang.enums.BaseEnum;

import java.util.function.Function;

/**
 *  命令类
 * @author yin.beibei
 * @date 2018/11/23 16:05
 */
public interface Comand<T extends BaseEnum,K,R> extends Type<T> {

    R execute(K k);

     Function<K,R> getExecutor();

     void  setExecutor(Executor<K,R> executor);
}
