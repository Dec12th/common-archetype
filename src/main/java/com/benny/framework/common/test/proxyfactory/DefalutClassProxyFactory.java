package com.benny.framework.common.test.proxyfactory;

import com.benny.framework.common.test.handler.DefaultMethodInterceptor;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.util.Assert;


/**
 * @author yin.beibei
 * @date 2018/12/7 17:09
 */
public class DefalutClassProxyFactory<T> implements InterfaceProxyFactory<T> {

    private MethodInterceptor methodInterceptor;

    public DefalutClassProxyFactory() {
        this.methodInterceptor = new DefaultMethodInterceptor();
    }

    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    @Override
    public T createInstance(Class<T> targetClass) {
        Assert.notNull(targetClass,"targetClass is not allow null");
        if (targetClass.isInterface()) {
           throw new IllegalArgumentException("interface is not support");
        } else {
            return createClassProxy(targetClass);
        }
    }

    T createClassProxy(Class<T> classType) {
        Assert.notNull(methodInterceptor,"invocationHandler is not allow null");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(classType);
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        enhancer.setCallback(methodInterceptor);
        return (T) enhancer.create();
    }
}
