package com.benny.framework.common.test.proxyfactory;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author yin.beibei
 * @date 2018/12/7 11:03
 */
//@Service
public class InterfaceMethodProxy implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("MethodInterceptorImpl:" + method.getName());
        System.out.println("1111"+method.getDeclaringClass());
        System.out.println("2222:"+method.getDeclaringClass().getAnnotations()[0].toString());
        System.out.println("pre");
        Object rebut = methodProxy.invokeSuper(o, objects);
        System.out.println("post");
        return rebut;
    }
}
