package com.benny.framework.common.framework.lang.manager;

import java.util.Collection;
import java.util.Map;

/**
 * @author yin.beibei
 * @date 2018/11/23 16:16
 */
public interface MapManager<K, T> extends Manager<K, T> {

    void init(Map<K, T> map);

    Collection<T> getAllList();

    Map<K, T> getAllMap();

    void append(T t);

    void remove(K k);

}
