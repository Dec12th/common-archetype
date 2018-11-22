package com.benny.framework.common.net.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:28
 */
@Data
public class EnvContext implements Serializable {
    private static final long serialVersionUID = -4194182291802212200L;
    /** 追踪信息 */
    private TraceInfo traceInfo;

    /** 站点token信息 */
    private WebToken webToken;

    /** 请求发起IP地址, ipv4 */
    private String ipAddress;
}
