package com.benny.framework.common.framework.lang;

import com.benny.framework.common.framework.lang.enums.BaseEnum;

import java.util.Map;

/**
 * @author yin.beibei
 * @date 2018/11/23 15:17
 */
public interface StrategyManager<E extends BaseEnum, S extends Strategy<E>> {
    /**
     * 设置策略
     * @param strategies 策略映射
     */
    void setStrategies(Map<E, S> strategies);

    /**
     * 获取所有策略
     * @return 所有策略
     */
    Map<E, S> getAllStrategies();

    /**
     * 追加策略
     * @param strategy     策略对象
     */
    void appendStrategy(S strategy);

    /**
     * 根据策略类型删除策略
     * @param strategyType 策略类型
     */
    void removeStrategy(E strategyType);

    /**
     * 根据策略类型查询策略对象
     * @param strategyType 策略类型
     * @return 策略对象
     */
    S getStrategy(E strategyType);
}
