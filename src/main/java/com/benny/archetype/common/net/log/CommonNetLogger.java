package com.benny.archetype.common.net.log;

import com.benny.archetype.common.net.domain.BaseRequest;
import com.benny.archetype.common.net.domain.BaseResult;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:23
 */
public interface CommonNetLogger {
    /**
     * 记录请求对象日志, 请求对象必须继承BaseRequest
     * @param invokePoint 调用点
     * @param request     请求对象
     */
    void logRequest(Object invokePoint, BaseRequest request);

    /**
     * 记录结果对象日志, 结果对象可以不继承BaseResult
     * @param invokePoint 调用点
     * @param result      结果对象
     */
    void logResult(Object invokePoint, BaseResult result);

    /**
     * 记录信息
     * @param message 信息
     */
    void info(String message);

    /**
     * 根据模板记录信息
     * @param format   模板
     * @param message  信息
     */
    void info(String format, Object... message);

    /**
     * 记录警告信息
     * @param message 信息
     */
    void warn(String message);

    /**
     * 根据模板记录警告信息
     * @param format   模板
     * @param message  信息
     */
    void warn(String format, Object... message);

    /**
     * 记录异常信息
     * @param message 信息
     */
    void error(String message);

    /**
     * 根据模板记录异常信息
     * @param format   模板
     * @param message  信息
     */
    void error(String format, Object... message);

    /**
     * 记录异常信息, 包含异常对象信息
     * @param message   异常信息
     * @param throwable 异常对象信息
     */
    void error(String message, Throwable throwable);
}
