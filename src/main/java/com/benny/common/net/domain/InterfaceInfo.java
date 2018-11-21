package com.benny.common.net.domain;

import lombok.Data;

import java.util.List;

/**
 * 接口信息
 *
 * @author yin.beibei
 * @date 2018/11/19 11:22
 */
@Data
public class InterfaceInfo {
    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 接口类
     */
    private Class interfaceClass;

    /**
     * 服务点信息
     */
    private List<ServicePointInfo> servicePointInfos;
}
