package com.benny.common.framework.lang.exception;

/**
 * 业务异常通用接口, <font color="red">必须配合extend RuntimeException使用</font><br>
 * @author yin.beibei
 * @date 2018/11/20 9:59
 */
public interface CommonException<C extends CommonErrorCode> {
    /**
     * 获取异常码
     * @return 异常码
     */
    C getErrorCode();

    /**
     * 设置异常码
     * @param errorCode 异常码
     */
    void setErrorCode(C errorCode);

    /**
     * 获取异常信息
     * @return 异常信息
     */
    String getMessage();

    /**
     * 获取额外的异常信息
     * @return 额外的异常信息
     */
    String getExtraMessage();

    /**
     * 设置异常信息
     */
    void setExtraMessage(String extraMessage);

    /**
     * 获取异常堆栈
     * @return 错误堆栈
     */
    StackTraceElement[] getStackTrace();

    /**
     * @param stackTraceElements 异常堆栈
     * 设置异常堆栈
     */
    void setStackTrace(StackTraceElement[] stackTraceElements);

}
