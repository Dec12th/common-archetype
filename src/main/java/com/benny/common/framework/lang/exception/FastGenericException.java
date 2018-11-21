package com.benny.common.framework.lang.exception;

/**
 * @author yin.beibei
 * @date 2018/11/20 10:08
 */
public class FastGenericException extends GenericException {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6172931801902534206L;

    /**
     * 构造方法
     *
     * @param errorCode 错误码
     */
    public FastGenericException(CommonErrorCode errorCode) {
        super(errorCode);
    }


    /**
     * 构造方法, 将业务异常转换为快速业务异常
     *
     * @param ex 业务异常
     */
    public FastGenericException(GenericException ex) {
        super(ex.getErrorCode(), ex.getExtraMessage());
    }

    /**
     * 构造方法
     *
     * @param errorCode    错误码
     * @param extraMessage 错误信息
     */
    public FastGenericException(CommonErrorCode errorCode, String extraMessage) {
        super(errorCode, extraMessage);
    }

    /**
     * 填充异常堆栈, 这里被替换为空运行且不做同步处理, 以加速异常对象的构造
     */
    @Override
    public Throwable fillInStackTrace() {
        return null;
    }

    /**
     * method for get errorCode
     */
    public CommonErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * method for set errorCode
     */
    public void setErrorCode(CommonErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * method for get extraMessage
     */
    public String getExtraMessage() {
        return extraMessage;
    }

    /**
     * method for set extraMessage
     */
    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }

}
