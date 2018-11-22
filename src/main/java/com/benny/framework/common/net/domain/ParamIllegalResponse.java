package com.benny.framework.common.net.domain;

/**
 * 参数异常回复
 * @author yin.beibei
 * @date 2018/11/22 16:25
 */
public final class ParamIllegalResponse extends BaseResponse {
    /**
     * 包内可见构造函数
     */
    ParamIllegalResponse() {
    }

    /**
     * @see BaseResponse#fillCodePairs()
     */
    @Override
    protected final void fillCodePairs() {
    }

    /**
     * @see BaseResponse#getCode()
     */
    @Override
    public final String getCode() {
        return ResponseConstants.PARAM_ILLEGAL_CODE;
    }

    /**
     * @see BaseResponse#getMessage()
     */
    @Override
    public final String getMessage() {
        return ResponseConstants.PARAM_ILLEGAL_MESSAGE;
    }


}
