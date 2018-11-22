package com.benny.framework.common.net.domain;

/**
 * @author yin.beibei
 * @date 2018/11/22 16:25
 */
public final class ResponseConstants {
    /** 成功返回码 */
    public static final String SUCCESS_CODE = "000000";

    /** 成功返回信息 */
    public static final String SUCCESS_MESSAGE = "成功";

    /** 系统异常返回码 */
    public static final String SYSTEM_ERROR_CODE = "000999";

    /** 系统异常返回信息 */
    public static final String SYSTEM_ERROR_MESSAGE = "系统异常";

    /** 参数异常返回码 */
    public static final String PARAM_ILLEGAL_CODE = "000998";

    /** 参数异常返回信息 */
    public static final String PARAM_ILLEGAL_MESSAGE = "参数非法";

    /** 参数异常返回对象 */
    public static final BaseResponse PARAM_ILLEGAL_RESPONSE = new ParamIllegalResponse();

}
