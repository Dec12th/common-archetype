package com.benny.framework.common.test.handler;

import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

/**
 * @author yin.beibei
 * @date 2018/12/7 10:57
 */
public abstract class AbstarctInterfaceInvocationHandler implements InvocationHandler {

    public abstract void preInvoke(Object proxy, Method method, Object[] args);

    public abstract void postInvoke(Object proxy, Method method, Object[] args);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("ObjectProxy execute:" + method.getName());
        preInvoke(proxy,method,args);
        Object result = method.invoke(proxy,args);
        postInvoke(proxy,method,args);
        return result;
    }

}
