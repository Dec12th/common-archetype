package com.benny.framework.common.framework.lang;

import com.benny.framework.common.framework.lang.enums.BaseEnum;
import com.benny.framework.common.framework.lang.exception.CommonErrorCode;
import com.benny.framework.common.framework.lang.exception.GenericException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 抽象策略管理实现类
 * @author yin.beibei
 * @date 2018/11/23 15:19
 */
public abstract class AbstractStrategyManagerImpl <E extends BaseEnum, S extends Strategy<E>> implements StrategyManager<E, S>{
    /** 日志记录器 */
    private static final Logger LOGGER = LoggerFactory.getLogger(StrategyManager.class);

    /** 策略映射 */
    private final Map<E, S> strategyMap = new HashMap<>();

    /**
     * @see StrategyManager#setStrategies(Map)
     */
    @Override
    public void setStrategies(Map<E, S> strategies)
    {
        LOGGER.info("初始化策略管理器: {}", getClass());
        synchronized (this)
        {
            strategyMap.clear();
            strategyMap.putAll(strategies);
        }
    }

    /**
     * @see StrategyManager#getAllStrategies()
     */
    @Override
    public Map<E, S> getAllStrategies()
    {
        return Collections.unmodifiableMap(strategyMap);
    }

    /**
     * @see StrategyManager#appendStrategy(Strategy)
     */
    @Override
    public void appendStrategy(S strategy)
    {
        if (strategy == null)
        {
            LOGGER.warn("追加策略时策略对象不存在");
            return;
        }

        if (strategy.getType() == null)
        {
            LOGGER.error("追加策略时策略对象的类型不存在, 策略对象: {}", strategy);
            throw new GenericException(CommonErrorCode.UNKNOWN_ERROR, "追加策略时策略对象的类型不存在");
        }

        if (strategyMap.keySet().contains(strategy.getType()))
        {
            LOGGER.error("管理器已包含相同类型的策略, 策略类型: {}", strategy.getType().getCode());
            throw new GenericException(CommonErrorCode.UNKNOWN_ERROR, "管理器已包含相同类型的策略");
        }
        strategyMap.put(strategy.getType(), strategy);
    }

    /**
     * @see StrategyManager#removeStrategy(BaseEnum)
     */
    @Override
    public void removeStrategy(E strategyType)
    {
        strategyMap.remove(strategyType);
    }

    /**
     * @see StrategyManager#getStrategy(BaseEnum) (Map)
     */
    @Override
    public S getStrategy(E strategyType)
    {
        return strategyMap.get(strategyType);
    }

}
