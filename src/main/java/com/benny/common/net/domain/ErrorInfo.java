package com.benny.common.net.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:27
 */
@Data
public class ErrorInfo implements Serializable {

    private static final long serialVersionUID = -5558413956243158583L;
    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误堆栈
     */
    private StackTraceElement[] stackTraceElements;
}
