package com.benny.archetype.common.net.exception;

import java.lang.reflect.Method;

/**
 * 参数风格{@link com.benny.archetype.common.net.resolver.SpringHttpInterfaceResolver} 不允许异常
 * @author yin.beibei
 * @date 2018/11/19 12:02
 */
public class ArgumentStyleNotAllowException extends RuntimeException {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7074834745109102519L;

    /**
     * 构造函数
     *
     * @param sourceInterfaceInfo 源接口信息
     * @param method              方法
     */
    public ArgumentStyleNotAllowException(Class sourceInterfaceInfo, Method method) {
        super(sourceInterfaceInfo.getName() + "类的" + method.getName() + "方法含有非法的参数定义, 正确的参数定义应该是单独一个继承了BaseRequest类的对象");
    }
}
