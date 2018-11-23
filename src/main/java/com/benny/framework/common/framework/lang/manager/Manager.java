package com.benny.framework.common.framework.lang.manager;

/**
 * @author yin.beibei
 * @date 2018/11/23 16:13
 */
public interface Manager<K,T> {

    T getInstance(K key);
}
