package com.benny.framework.common.test;

import lombok.Data;

/**
 * @author yin.beibei
 * @date 2018/12/3 17:48
 */
@Data
@CustomComponent
public class User {
    private String name;

    @MethodProxy
    public String hello() {
        System.out.println("invoke ....");
        return "hello!";
    }

    public static void main(String[] args) {
        System.out.println(11);
    }
}
