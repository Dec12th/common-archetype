package com.benny.framework.common.test.handler;

import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @ClassName: MethodProxyInterceptor
 * @Description:
 * @author:大贝
 * @date:2018年12月08日 21:19
 */
public class MethodProxyInterceptor extends AbstactMethodInterceptor {
    @Override
    public void preInvoke(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        if (!method.isAnnotationPresent(com.benny.framework.common.test.annotations.MethodProxy.class)) {
            return;
        }
        System.out.println("MethodProxyInterceptor preInvoke......");
    }

    @Override
    public void postInvoke(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        if (!method.isAnnotationPresent(com.benny.framework.common.test.annotations.MethodProxy.class)) {
            return;
        }
        System.out.println("MethodProxyInterceptor postInvoke......");
    }
}
