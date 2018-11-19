package com.benny.archetype.common.lang.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 枚举类基类
 * @author yin.beibei
 * @date 2018/11/19 17:01
 */
public interface BaseEnum<T extends Enum<T>> {

    /**
     * 获得枚举对象的code
     * @return 枚举对象的code
     */
    String getCode();

    /**
     * 获得枚举对象那个的描述
     * @return 枚举对象的描述
     */
    String getDescription();

    /**
     * 根据枚举码获取枚举对象
     *
     * @param code
     *            枚举码
     * @return 枚举对象
     */
    T getByCode(String code);
}
