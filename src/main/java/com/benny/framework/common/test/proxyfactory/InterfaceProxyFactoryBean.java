package com.benny.framework.common.test.proxyfactory;

import com.benny.framework.common.test.User;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.MethodInterceptor;


/**
 * @author yin.beibei
 * @date 2018/12/7 10:13
 */
public class InterfaceProxyFactoryBean<T> implements InitializingBean, FactoryBean<T> {

    private String innerClassName;

    private MethodInterceptor methodInterceptor;

    private InvocationHandler invocationHandler;

    public void setInnerClassName(String innerClassName) {
        this.innerClassName = innerClassName;
    }

    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    public void setInvocationHandler(InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    @Override
    public T getObject() throws Exception {
        Class innerClass = Class.forName(innerClassName);
        if (innerClass.isInterface()) {
            DefalutInterfaceProxyFactory<T> interfaceProxyFactory = new DefalutInterfaceProxyFactory<>();
            if (invocationHandler!=null) {
                interfaceProxyFactory.setInvocationHandler(invocationHandler);
            }
            return (T) interfaceProxyFactory.createInstance(innerClass);
        } else {
            DefalutClassProxyFactory<T> defalutClassProxyFactory = new DefalutClassProxyFactory<>();
            if (methodInterceptor!=null) {
                defalutClassProxyFactory.setMethodInterceptor(methodInterceptor);
            }
            return (T) defalutClassProxyFactory.createInstance(innerClass);
        }
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return Class.forName(innerClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public static void main(String[] args) throws Exception {
        InterfaceProxyFactoryBean factoryBeanTest = new InterfaceProxyFactoryBean();
        factoryBeanTest.setInnerClassName(User.class.getName());
        User user = (User) factoryBeanTest.getObject();
        System.out.println(user.hello());
    }
}
