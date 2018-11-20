package com.benny.archetype.common.net.exception;

/**
 * @ClassName: NoInstancesException
 * @Description:
 * @author:大贝
 * @date:2018年11月20日 22:11
 */
public class NoInstancesException extends RuntimeException {
    /**
     * 没有找到实例
     *
     * @param instanceName 实例名
     */
    public NoInstancesException(String instanceName) {
        super("没有找到 [" + instanceName + "] 实例主机");
    }
}
