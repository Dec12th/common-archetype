package com.benny.framework.common.test.proxyfactory;

/**
 * @author yin.beibei
 * @date 2018/12/7 17:07
 */
public interface InterfaceProxyFactory<T> {

    T createInstance(Class<T> classType);
}
