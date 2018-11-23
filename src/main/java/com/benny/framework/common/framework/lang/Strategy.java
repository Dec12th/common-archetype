package com.benny.framework.common.framework.lang;

import com.benny.framework.common.framework.lang.enums.BaseEnum;

/**
 * @author yin.beibei
 * @date 2018/11/23 15:17
 */
public interface Strategy<T extends BaseEnum> {
    /**
     * 获取类型
     * @return 类型
     */
    T getType();
}
