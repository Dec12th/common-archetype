package com.benny.common.net.domain;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author yin.beibei
 * @date 2018/11/19 11:23
 */
@Data
public abstract class ServicePointInfo {
    /**
     * 接口类
     */
    protected Class interfaceClass;

    /**
     * 客户端的服务点
     */
    protected Method serviceClientPoint;

    /**
     * 服务端的服务点
     */
    protected String serviceServerPoint;

    /**
     * 结果类型
     */
    protected Class resultType;

    /**
     * 参数类型队列
     */
    protected Class argumentType;

}
