package com.benny.archetype.common.framework.service.template;

/**
 * 业务执行回调类
 * @author yin.beibei
 * @date 2018/11/19 17:39
 */
public abstract class ServiceCallBack {
    /**
     * 业务执行前的检查
     * @throws Exception
     */
    public abstract void check() throws Exception;

    /**
     * 业务具体实现
     * @throws Exception
     */
    public abstract void doService() throws Exception;


    /**
     * 服务后操作
     * @throws Exception
     */
    public abstract void afterService() throws Exception;
}
