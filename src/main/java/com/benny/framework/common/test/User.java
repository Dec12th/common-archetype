package com.benny.framework.common.test;

import com.benny.framework.common.test.annotations.CustomComponent;
import lombok.Data;

/**
 * @author yin.beibei
 * @date 2018/12/3 17:48
 */
@Data
@CustomComponent
public class User {
    private String name;


    public String hello() {
        System.out.println("User invoke ....");
        return "hello!";
    }

//    @MethodProxy
//    public void methProxy() {
//        System.out.println("methProxy invoke ....");
//    }
}
