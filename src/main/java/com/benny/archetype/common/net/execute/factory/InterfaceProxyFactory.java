package com.benny.archetype.common.net.execute.factory;

/**
 * 接口代理工厂
 * @author yin.beibei
 * @date 2018/11/19 10:51
 */
public interface InterfaceProxyFactory {
    /**
     * 创建代理对象
     * @param interfaceType 接口类
     * @param <T>           接口类泛型
     * @return 代理对象
     */
    <T> T createProxy(Class<T> interfaceType);
}
