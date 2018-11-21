package com.benny.framework.common.net.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:26
 */
@Data
public class ErrorContext implements Serializable {
    private static final long serialVersionUID = 8278385260929928622L;
    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 异常信息
     */
    private String errorMessage;

    /**
     * 异常信息, 包含堆栈信息等
     */
    private List<ErrorInfo> errorInfos;
}
