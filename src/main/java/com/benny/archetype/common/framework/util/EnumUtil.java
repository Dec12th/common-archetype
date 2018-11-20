package com.benny.archetype.common.framework.util;

import org.apache.commons.lang.enums.EnumUtils;

/**
 * @author yin.beibei
 * @date 2018/11/20 9:37
 */
public class EnumUtil {
    public static Enum getByCode(Class enumClass,String code) {
        return Enum.valueOf(enumClass,code);
    }
}
