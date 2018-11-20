package com.benny.archetype.common.framework.service.template;

/**
 * 可降级回调类
 *
 * @author yin.beibei
 * @date 2018/11/20 10:15
 */
public abstract class FallbackableServiceCallBack extends ServiceCallBack {
    /**
     * 降级策略, 没有降级策略不用关心
     * 需要搭配可降级业务模板执行
     *
     * @throws Exception
     */
    public abstract void fallback() throws Exception;
}
