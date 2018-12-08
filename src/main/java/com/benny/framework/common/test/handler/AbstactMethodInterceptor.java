package com.benny.framework.common.test.handler;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author yin.beibei
 * @date 2018/12/7 17:47
 */
public abstract class AbstactMethodInterceptor implements MethodInterceptor {

    public abstract void preInvoke(Object o, Method method, Object[] objects, MethodProxy methodProxy);

    public abstract void postInvoke(Object o, Method method, Object[] objects, MethodProxy methodProxy);

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        preInvoke(o,method,objects,methodProxy);
        Object result = methodProxy.invokeSuper(o, objects);
        postInvoke(o,method,objects,methodProxy);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(11);
    }
}
