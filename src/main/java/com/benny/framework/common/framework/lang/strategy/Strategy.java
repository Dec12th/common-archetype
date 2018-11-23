package com.benny.framework.common.framework.lang.strategy;

import com.benny.framework.common.framework.lang.Type;
import com.benny.framework.common.framework.lang.enums.BaseEnum;

/**
 * 策略类
 * @author yin.beibei
 * @date 2018/11/23 15:17
 */
public interface Strategy<T extends BaseEnum> extends Type<T> {
    /**
     * @param k 策略执行业务主逻辑的参数类型
     * @return R 策略执行业务主逻辑的返回参数类型
     */
    <K,R> R execute(K k);
}
