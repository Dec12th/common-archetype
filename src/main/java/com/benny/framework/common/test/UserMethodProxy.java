package com.benny.framework.common.test;

import com.benny.framework.common.test.annotations.MethodProxy;

/**
 * @ClassName: UserMethodProxy
 * @Description:
 * @author:大贝
 * @date:2018年12月08日 21:25
 */
//@Service
public class UserMethodProxy {

    @MethodProxy
    public void methProxy() {
        System.out.println("methProxy invoke ....");
    }

    public String hello() {
        System.out.println("invoke ....");
        return "hello!";
    }
}
