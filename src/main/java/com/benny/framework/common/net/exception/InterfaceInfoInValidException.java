package com.benny.framework.common.net.exception;

/**
 * @author yin.beibei
 * @date 2018/11/19 11:51
 */
public class InterfaceInfoInValidException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7901773001636418794L;

    /**
     * 构造函数
     */
    public InterfaceInfoInValidException() {
        super("接口信息不合法!");
    }
}
