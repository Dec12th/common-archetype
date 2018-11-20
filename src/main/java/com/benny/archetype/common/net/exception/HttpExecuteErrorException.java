package com.benny.archetype.common.net.exception;

import okhttp3.Response;
import org.springframework.http.HttpEntity;

/**
 * @ClassName: HttpExecuteErrorException
 * @Description:
 * @author:大贝
 * @date:2018年11月20日 22:12
 */
public class HttpExecuteErrorException extends RuntimeException {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -524379777219983097L;

    /**
     * 构造函数
     *
     * @param httprequest http请求
     */
    public HttpExecuteErrorException(HttpEntity httprequest) {
        super("执行http请求时发生异常, http请求: " + httprequest.toString());
    }

    /**
     * 构造函数
     *
     * @param response http結果
     */
    public HttpExecuteErrorException(Response response) {
        super("执行http请求时发生异常, http結果: " + response.toString());
    }
}
