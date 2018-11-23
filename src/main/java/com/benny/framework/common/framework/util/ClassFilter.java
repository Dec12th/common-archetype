package com.benny.framework.common.framework.util;

/**
 * 类过滤器
 *
 * @author yin.beibei
 * @date 2018/11/23 15:12
 */
public interface ClassFilter {
    /**
     * 判断指定的类是否被拒绝
     *
     * @param clazz 类
     * @return 指定的类是否被拒绝
     */
    boolean reject(Class clazz);
}
