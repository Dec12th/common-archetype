package com.benny.framework.common.net.domain;

import lombok.Data;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:29
 */
@Data
public class TraceInfo {

    /** 追踪id, 标记一次业务调用 */
    private String traceId;

    /** 子追踪id, 标记一次微服务调用 */
    private String spanId;
}
