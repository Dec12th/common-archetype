package com.benny.common.framework.util;

import com.benny.common.framework.config.CommonConfig;
import com.benny.common.framework.config.ConfigKeyEnum;
import com.benny.common.framework.constant.Constants;
import com.benny.common.framework.lang.enums.CommonScenario;
import com.benny.common.framework.lang.exception.CommonErrorCode;
import com.benny.common.framework.lang.exception.CommonException;
import com.benny.common.net.domain.ErrorContext;
import com.benny.common.net.domain.ErrorInfo;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yin.beibei
 * @date 2018/11/20 10:02
 */
public class ErrorContextBuilder {
    /**
     * 异常码前缀
     */
    private static final String ERROR_CODE_PREFIX = getErrorCodePrefix();

    /**
     * 异常码版本号
     */
    private static final String ERROR_CODE_VERSION = getErrorCodeVersion();

    /**
     * 异常堆栈深度, 代表屏蔽前几层异常堆栈信息
     */
    private static final Integer ERROR_BUILDER_STACK_DEPTH = getStackDepth();

    /**
     * 异常堆栈长度, 代表保存几层异常堆栈信息
     */
    private static final Integer ERROR_BUILDER_STACK_LENGTH = getStackLength();


    /**
     * 构建错误码
     *
     * @param commonScenario  场景码
     * @param commonErrorCode 业务异常码
     * @return 异常码
     */
    public static String buildErrorCode(CommonScenario commonScenario, CommonErrorCode commonErrorCode) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.
                // 前缀
                        append(ERROR_CODE_PREFIX).
                // 异常码版本
                        append(ERROR_CODE_VERSION).
                // 异常等级
                        append(commonErrorCode.getInfoLevel().getCode()).
                // 异常类型
                        append(commonErrorCode.getInfoType().getCode()).
                // 应用编码
                        append(commonScenario.getAppCode()).
                // 场景码
                        append(commonScenario.getCode()).
                // 异常码
                        append(commonErrorCode.getCode());
        return stringBuilder.toString();
    }

    /**
     * 构建异常上下文
     *
     * @param commonScenario 场景码
     * @param e              业务异常
     * @return 异常上下文
     */
    public static ErrorContext buildErrorContext(CommonScenario commonScenario, CommonException e) {
        ErrorContext errorContext = new ErrorContext();
        CommonErrorCode commonErrorCode = e.getErrorCode();
        String errorCode = buildErrorCode(commonScenario, commonErrorCode);


        errorContext.setErrorMessage(e.getMessage());
        errorContext.setErrorCode(errorCode);

        List<ErrorInfo> errorInfos = errorContext.getErrorInfos();

        if (CollectionUtils.isEmpty(errorInfos)) {
            errorInfos = new ArrayList<>();
            errorContext.setErrorInfos(errorInfos);
        }

        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorCode(errorCode);
        errorInfo.setStackTraceElements(getStackTraceElementsWithDepth(e, ERROR_BUILDER_STACK_DEPTH,
                ERROR_BUILDER_STACK_LENGTH));

        errorInfos.add(errorInfo);

        return errorContext;
    }

    /**
     * 根据深度信息获取堆栈跟踪信息, 此方法会屏蔽最先弹出的 $depth 层堆栈信息
     *
     * @param e     异常
     * @param depth 堆栈深度
     */
    private static StackTraceElement[] getStackTraceElementsWithDepth(CommonException e, int depth, int length) {
        if (length <= 0) {
            return null;
        }

        StackTraceElement[] sourceStackTraceElements = e.getStackTrace();

        // 原始堆栈深度减去屏蔽堆栈深度后剩余的深度
        int leaseDepth = sourceStackTraceElements.length - depth;
        if (leaseDepth < 0) {
            leaseDepth = 0;
        }

        int targetLength = leaseDepth > length ? length : leaseDepth;

        if (leaseDepth > 0) {
            StackTraceElement[] targetStackTraceElements = new StackTraceElement[targetLength];

            for (int targetDepth = 0; targetDepth < targetLength; targetDepth++) {
                int sourceDepth = targetDepth + depth;
                targetStackTraceElements[targetDepth] = sourceStackTraceElements[sourceDepth];
            }

            return targetStackTraceElements;
        } else {
            return null;
        }
    }

    /**
     * 获取异常码前缀
     *
     * @return 异常码前缀
     */
    private static String getErrorCodePrefix() {
        String errorCodePrefix = CommonConfig.getProperty(ConfigKeyEnum.BIZ_ERROR_BUILDER_ERROR_CODE_PREFIX);
        if (errorCodePrefix == null) {
            return Constants.DEFAULT_ERROR_BUILDER_ERROR_CODE_PREFIX;
        } else {
            return errorCodePrefix;
        }
    }

    /**
     * 获取异常码版本号
     *
     * @return 异常码版本号
     */
    private static String getErrorCodeVersion() {
        String errorCodeVersion = CommonConfig.getProperty(ConfigKeyEnum.BIZ_ERROR_BUILDER_ERROR_CODE_VERSION);
        if (errorCodeVersion == null) {
            return Constants.DEFAULT_ERROR_BUILDER_ERROR_CODE_VERSION;
        } else {
            return errorCodeVersion;
        }
    }

    /**
     * 获取异常堆栈深度
     *
     * @return 异常堆栈深度
     */
    private static Integer getStackDepth() {
        Integer errorBuilderStackDepth = CommonConfig.getProperty(ConfigKeyEnum.BIZ_ERROR_BUILDER_STACK_DEPTH);
        if (errorBuilderStackDepth == null) {
            return Constants.DEFAULT_ERROR_BUILDER_STACK_DEPTH;
        } else {
            return errorBuilderStackDepth;
        }
    }

    /**
     * 获取异常堆栈长度
     *
     * @return 异常堆栈长度
     */
    private static Integer getStackLength() {
        Integer errorBuilderStackLength = CommonConfig.getProperty(ConfigKeyEnum.BIZ_ERROR_BUILDER_STACK_LENGTH);
        if (errorBuilderStackLength == null) {
            return Constants.DEFAULT_ERROR_BUILDER_STACK_LENGTH;
        } else {
            return errorBuilderStackLength;
        }
    }

}
