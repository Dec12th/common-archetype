package com.benny.common.framework.constant;

import com.benny.common.framework.lang.exception.CommonErrorCode;
import com.benny.common.framework.lang.exception.FastGenericException;
import com.benny.common.framework.lang.exception.GenericException;
import com.benny.common.framework.util.ErrorContextBuilder;

import java.nio.charset.Charset;

/**
 * @author yin.beibei
 * @date 2018/11/19 17:44
 */
public class Constants {


    /*    ==============================一些通用常量==============================   */

    /**
     * 默认异常上下文构造器异常码前缀 {@link ErrorContextBuilder#ERROR_CODE_PREFIX}
     */
    public static final String DEFAULT_ERROR_BUILDER_ERROR_CODE_PREFIX = "CP";

    /**
     * 默认异常上下文构造器异常码版本 {@link ErrorContextBuilder#ERROR_CODE_VERSION}
     */
    public static final String DEFAULT_ERROR_BUILDER_ERROR_CODE_VERSION = "1";

    /**
     * 默认异常上下文构造器异常堆栈深度 {@link ErrorContextBuilder#ERROR_BUILDER_STACK_DEPTH}
     */
    public static final Integer DEFAULT_ERROR_BUILDER_STACK_DEPTH = 0;

    /**
     * 默认异常上下文构造器异常堆栈长度 {@link ErrorContextBuilder#ERROR_BUILDER_STACK_LENGTH}
     */
    public static final Integer DEFAULT_ERROR_BUILDER_STACK_LENGTH = 1000;

    /**
     * 默认编码名
     */
    public static final String DEFAULT_CHARSET_NAME = "utf-8";

    /**
     * 默认编码对象
     */
    public static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);


    /*    ==============================场景码==============================   */

    /**
     * 通用场景码
     */
    public static final String COMMON_SCENARIO_CODE = "0000";

    /**
     * 通用场景描述
     */
    public static final String COMMON_SCENARIO_DESCRIPTION = "通用打印场景";

    /*    ==============================异常码==============================   */

    /**
     * 未知异常码
     */
    public static final String UNKNOWN_ERROR_CODE = "999";

    /**
     * 未知异常描述
     */
    public static final String UNKNOWN_ERROR_DESCRIPTION = "未知异常";

    /**
     * 参数异常码
     */
    public static final String PARAM_ILLEGAL_ERROR_CODE = "998";

    /**
     * 参数异常描述
     */
    public static final String PARAM_ILLEGAL_ERROR_DESCRIPTION = "参数异常";


    /*    ==============================通用异常==============================   */

    /**
     * 非法调用异常, 快速异常对象
     */
    public static final GenericException ILLEGAL_METHOD_ERROR = new FastGenericException(CommonErrorCode.UNKNOWN_ERROR, "非法的方法");

    /**
     * 未知异常, 快速异常对象
     */
    public static final GenericException UNKNOWN_ERROR = new FastGenericException(CommonErrorCode.UNKNOWN_ERROR, "未知异常");



    /*    ==============================特殊含义字符串==============================   */

    /**
     * 置空字符串定义
     */
    public static final String FOR_NULL_STRING = "★☆★☆★★☆★★☆★★☆★☆★★☆★★☆★☆★★☆★★☆★★☆★☆★★☆★★☆★";


}
