package com.benny.framework.common.net.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yin.beibei
 * @date 2018/11/22 16:22
 */
public abstract class BaseResponse implements Serializable {

    private static final long serialVersionUID = -3115640318975421975L;
    /**
     * 返回码
     */
    private String code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 类返回码映射
     */
    @JsonIgnore
    private static Map<Class<? extends BaseResponse>, Map<String, ImmutablePair<String, String>>> classCodeMap = new ConcurrentHashMap<>();

    /**
     * 返回码映射
     */
    @JsonIgnore
    private Map<String, ImmutablePair<String, String>> codeMap = classCodeMap.computeIfAbsent(getClass(), map -> new HashMap<>());

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseResponse.class);

    /**
     * 构造函数, 载入返回码映射
     */
    public BaseResponse() {
        fillCodePairs();
    }

    /**
     * 填充结果状态, 仅需要转换异常码的情况下使用此方法
     *
     * @param result 结果
     */
    @SuppressWarnings("unchecked")
    public void fillResponseState(BaseResult result) {
        fillResponseState(result, null);
    }

    /**
     * 填充结果状态
     *
     * @param result         结果
     * @param resultCallback 结果填充回调
     */
    @SuppressWarnings("unchecked")
    public void fillResponseState(BaseResult result, ResultCallback resultCallback) {
        if (result == null) {
            LOGGER.warn("结果对象不存在");
            systemError();
            return;
        }

        if (result.isSuccess()) {
            this.code = ResponseConstants.SUCCESS_CODE;
            this.message = ResponseConstants.SUCCESS_MESSAGE;

            if (resultCallback != null) {
                try {
                    resultCallback.onSuccess();
                } catch (Exception e) {
                    LOGGER.warn("处理业务成功逻辑时发生异常", e);
                    systemError();
                }
            }

            return;
        }


        ErrorContext errorContext = result.getErrorContext();
        if (errorContext == null) {
            LOGGER.warn("业务结果不成功, 且异常上下文不存在");
            systemError();
            return;
        }

        LOGGER.warn("业务结果不成功, 异常信息: [{}]", errorContext);
        if (resultCallback != null) {
            try {
                resultCallback.onFail();
            } catch (Exception e) {
                LOGGER.warn("处理业务失败逻辑时发生异常", e);
                systemError();
            }
        }


        ImmutablePair<String, String> errorResponsePair = codeMap.get(errorContext.getErrorCode());
        if (errorResponsePair == null) {
            systemError();
            return;
        }

        this.code = errorResponsePair.getLeft();
        this.message = errorResponsePair.getRight();
    }

    /**
     * 填充返回码映射
     */
    protected abstract void fillCodePairs();

    /**
     * 填充返回码映射
     *
     * @param errorCode    异常码
     * @param responseCode 返回码
     */
    protected void putCodePair(String errorCode, String responseCode, String responseMessage) {
        if (codeMap == null) {
            synchronized (getClass()) {
                if (codeMap == null) {
                    codeMap = new HashMap<>();
                    classCodeMap.putIfAbsent(getClass(), codeMap);
                }
            }
        }

        codeMap.putIfAbsent(errorCode, new ImmutablePair<>(responseCode, responseMessage));
    }

    /**
     * 系统异常
     */
    private void systemError() {
        this.code = ResponseConstants.SYSTEM_ERROR_CODE;
        this.message = ResponseConstants.SYSTEM_ERROR_MESSAGE;
    }

    /**
     * method for get code
     */
    public String getCode() {
        return code;
    }

    /**
     * method for set code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * method for get message
     */
    public String getMessage() {
        return message;
    }

    /**
     * method for set message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
