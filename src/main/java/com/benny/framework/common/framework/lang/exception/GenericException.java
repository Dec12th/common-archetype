package com.benny.framework.common.framework.lang.exception;

import com.benny.framework.common.framework.util.ErrorContextBuilder;
import com.benny.framework.common.framework.util.ScenarioHolder;

/**
 * @author yin.beibei
 * @date 2018/11/20 10:01
 */
public class GenericException extends RuntimeException implements CommonException<CommonErrorCode> {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6172931801902534206L;

    /**
     * 异常码, 尾码
     */
    protected CommonErrorCode errorCode;

    /**
     * 错误信息
     */
    protected String extraMessage;

    /**
     * 构造方法
     *
     * @param errorCode 错误码
     */
    public GenericException(CommonErrorCode errorCode) {
        super(assembleExceptionMessage(errorCode, null));
        this.errorCode = errorCode;
        this.extraMessage = errorCode.getDescription();
    }

    /**
     * 构造方法
     *
     * @param errorCode 错误码
     * @param cause     原始异常信息
     */
    public GenericException(CommonErrorCode errorCode, Throwable cause) {
        super(assembleExceptionMessage(errorCode, null), cause);
        this.errorCode = errorCode;
        this.extraMessage = errorCode.getDescription();
    }

    /**
     * 构造方法
     *
     * @param errorCode    错误码
     * @param extraMessage 错误信息
     */
    public GenericException(CommonErrorCode errorCode, String extraMessage) {
        super(assembleExceptionMessage(errorCode, extraMessage));
        this.errorCode = errorCode;
        this.extraMessage = extraMessage;
    }

    /**
     * 构造方法
     *
     * @param errorCode    错误码
     * @param extraMessage 错误信息
     * @param cause        原始异常信息
     */
    public GenericException(CommonErrorCode errorCode, String extraMessage, Throwable cause) {
        super(assembleExceptionMessage(errorCode, extraMessage), cause);
        this.errorCode = errorCode;
        this.extraMessage = extraMessage;
    }

    /**
     * 组装异常信息
     *
     * @param errorCode    异常码对象
     * @param extraMessage 额外信息
     * @return 异常信息
     */
    private static String assembleExceptionMessage(CommonErrorCode errorCode, String extraMessage) {
        String errorCodeStr = ErrorContextBuilder.buildErrorCode(ScenarioHolder.get(), errorCode);
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setErrorCode(errorCodeStr);
        exceptionMessage.setNormalMessage(errorCode.getDescription());
        exceptionMessage.setExtraMessage(extraMessage);

        return exceptionMessage.toStringWithoutClassName();
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

/**
 * 异常信息
 */
class ExceptionMessage {
    /**
     * 异常码
     */
    private String errorCode;

    /**
     * 普通异常信息
     */
    private String normalMessage;

    /**
     * 额外异常信息
     */
    private String extraMessage;

    /**
     * method for get errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * method for set errorCode
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * method for get normalMessage
     */
    public String getNormalMessage() {
        return normalMessage;
    }

    /**
     * method for set normalMessage
     */
    public void setNormalMessage(String normalMessage) {
        this.normalMessage = normalMessage;
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

    /**
     * 返回不带类名的对象信息
     *
     * @return 不带类名的对象信息
     */
    public String toStringWithoutClassName() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("errorCode='").append(errorCode).append('\'');
        sb.append(", normalMessage='").append(normalMessage).append('\'');
        sb.append(", extraMessage='").append(extraMessage).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExceptionMessage{");
        sb.append("errorCode='").append(errorCode).append('\'');
        sb.append(", normalMessage='").append(normalMessage).append('\'');
        sb.append(", extraMessage='").append(extraMessage).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
