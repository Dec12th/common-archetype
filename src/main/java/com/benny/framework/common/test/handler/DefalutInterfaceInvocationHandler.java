package com.benny.framework.common.test.handler;

import java.lang.reflect.Method;

/**
 * @author yin.beibei
 * @date 2018/12/7 17:46
 */
public class DefalutInterfaceInvocationHandler extends AbstarctInterfaceInvocationHandler {
    @Override
    public void preInvoke(Object proxy, Method method, Object[] args) {
        System.out.println("DefalutInterfaceInvocationHandler preInvoke......");
    }

    @Override
    public void postInvoke(Object proxy, Method method, Object[] args) {
        System.out.println("DefalutInterfaceInvocationHandler postInvoke......");
    }

    public static void main(String[] args) {
        System.out.println(11);
    }
}
