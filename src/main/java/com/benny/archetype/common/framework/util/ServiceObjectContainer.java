package com.benny.archetype.common.framework.util;

import java.util.HashMap;

/**
 * 服务对象容器, 线程变量
 *
 * @author yin.beibei
 * @date 2018/11/20 10:15
 */
public class ServiceObjectContainer {
    /**
     * 线程变量
     */
    private static ThreadLocal<HashMap<String, Object>> threadLocal;

    /**
     * 初始化
     */
    static {
        threadLocal = new ThreadLocal<>();
    }

    /**
     * 将对象以key值存入容器
     *
     * @param key   key
     * @param value 对象
     */
    public static void put(String key, Object value) {
        init();
        threadLocal.get().put(key, value);
    }

    /**
     * 根据key值取出对象
     *
     * @param key key
     * @return 对象
     */
    public static <T> T get(String key) {
        init();
        return (T) threadLocal.get().get(key);
    }

    /**
     * 清理容器
     */
    public static void clear() {
        init();
        threadLocal.get().clear();
    }

    /**
     * 线程维度初始化, 载入容器
     */
    private static void init() {
        if (threadLocal.get() == null) {
            threadLocal.set(new HashMap<String, Object>());
        }
    }

}
