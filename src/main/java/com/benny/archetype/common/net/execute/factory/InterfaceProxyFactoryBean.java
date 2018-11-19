package com.benny.archetype.common.net.execute.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 接口代理工厂生成对象, 用于xml配置的接口代理对象注入
 * @author yin.beibei
 * @date 2018/11/19 10:51
 */
public class InterfaceProxyFactoryBean implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {
    /**
     * 接口代理对象
     */
    private Object interfaceProxy;

    /**
     * 接口类型
     */
    private Class interfaceType;

    /**
     * 接口代理工厂
     */
    @Autowired
    private InterfaceProxyFactory interfaceProxyFactory;

    /**
     * 构造函数
     */
    public InterfaceProxyFactoryBean() {
    }

    /**
     * 构造函数, 用于动态生成bean时查找的构造函数
     *
     * @param interfaceType 接口类
     */
    public InterfaceProxyFactoryBean(Class interfaceType) {
        this.interfaceType = interfaceType;
    }

    /**
     * @see FactoryBean#getObject()
     */
    @Override
    public Object getObject() throws Exception {
        return interfaceProxy;
    }

    /**
     * @see FactoryBean#getObjectType()
     */
    @Override
    public Class<?> getObjectType() {
        return interfaceType;
    }

    /**
     * @see FactoryBean#isSingleton()
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * @see InitializingBean#afterPropertiesSet()
     */
    @Override
    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() throws Exception {
        this.interfaceProxy = interfaceProxyFactory.createProxy(interfaceType);
    }

    /**
     * method for get interfaceProxy
     */
    public Object getInterfaceProxy() {
        return interfaceProxy;
    }

    /**
     * method for set interfaceProxy
     */
    public void setInterfaceProxy(Object interfaceProxy) {
        this.interfaceProxy = interfaceProxy;
    }

    /**
     * method for get interfaceType
     */
    public Class getInterfaceType() {
        return interfaceType;
    }

    /**
     * method for set interfaceType
     */
    public void setInterfaceType(Class interfaceType) {
        this.interfaceType = interfaceType;
    }

    /**
     * method for get interfaceProxyFactory
     */
    public InterfaceProxyFactory getInterfaceProxyFactory() {
        return interfaceProxyFactory;
    }

    /**
     * @see ApplicationContextAware#setApplicationContext(ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    }
}
