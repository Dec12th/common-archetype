package com.benny.framework.common.test.handler;

import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author yin.beibei
 * @date 2018/12/7 17:50
 */
public class DefaultMethodInterceptor extends AbstactMethodInterceptor {
    @Override
    public void preInvoke(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        System.out.println("DefaultMethodInterceptor preInvoke......");
    }

    @Override
    public void postInvoke(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        System.out.println("DefaultMethodInterceptor postInvoke......");
    }
}
