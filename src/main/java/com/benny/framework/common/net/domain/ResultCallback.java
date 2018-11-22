package com.benny.framework.common.net.domain;

/**
 * 结果回调
 * @author yin.beibei
 * @date 2018/11/22 16:24
 */
public abstract class ResultCallback {
    /**
     * 成功时执行的回调
     */
    public abstract void onSuccess() throws Exception;

    /**
     * 失败时执行的回调
     */
    public void onFail() throws Exception {}

}
