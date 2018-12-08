package com.benny.framework.common.test.proxyfactory;

import com.benny.framework.common.test.handler.DefalutInterfaceInvocationHandler;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.util.Assert;


/**
 * @author yin.beibei
 * @date 2018/12/7 17:09
 */
public class DefalutInterfaceProxyFactory<T> implements InterfaceProxyFactory<T> {

    private InvocationHandler invocationHandler;

    public DefalutInterfaceProxyFactory() {
        this.invocationHandler = new DefalutInterfaceInvocationHandler();
    }

    public void setInvocationHandler(InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    @Override
    public T createInstance(Class<T> targetClass) {
        Assert.notNull(targetClass,"targetClass is not allow null");
        if (targetClass.isInterface()) {
            return createInterfaceProxy(targetClass);
        } else {
            throw new IllegalArgumentException("The target class must be an interface");
        }
    }

    T createInterfaceProxy(Class<T> classType) {
        Assert.notNull(invocationHandler,"invocationHandler is not allow null");
        ClassLoader classLoader = classType.getClassLoader();
        Class[] interfaces = new Class[]{classType};
        return (T) Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
    }

}
