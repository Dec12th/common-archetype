package com.benny.framework.common.framework.lang;

import com.benny.framework.common.framework.lang.enums.BaseEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象策略管理实现类, 支持Spring容器
 * @author yin.beibei
 * @date 2018/11/23 15:24
 */
public class AbstractSpringStrategyManagerImpl<E extends BaseEnum, S extends Strategy<E>> extends AbstractStrategyManagerImpl<E, S> {
    /**
     * 设置列表
     */
    @Autowired(required = false)
    public void setStrategies(List<S> strategyList) {
        Map<E, S> strategyMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(strategyList)) {
            strategyList.forEach(
                    strategy -> strategyMap.put(strategy.getType(), strategy));
        }
        setStrategies(strategyMap);
    }
}
