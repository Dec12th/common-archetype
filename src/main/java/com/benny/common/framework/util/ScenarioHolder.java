package com.benny.common.framework.util;

import com.benny.common.framework.lang.enums.CommonScenario;

/**
 * @author yin.beibei
 * @date 2018/11/19 17:42
 */
public class ScenarioHolder {
    /**
     * 保存场景信息的线程变量
     */
    private static ThreadLocal<CommonScenario> scenarioThreadLocal;

    /**
     * 初始化线程变量
     */
    static {
        scenarioThreadLocal = new ThreadLocal<>();
    }

    /**
     * 私有构造函数, 常规无法生成新对象
     */
    private ScenarioHolder() {

    }

    /**
     * 获取当前线程的场景信息, 如果该信息未设置, 则返回通常场景信息
     *
     * @return 当前线程的场景信息
     */
    @SuppressWarnings("unchecked")
    public static <T extends CommonScenario> T get() {
        T scenario = (T) scenarioThreadLocal.get();
        if (scenario == null) {
            return (T) CommonScenario.COMMON;
        }
        return scenario;
    }

    /**
     * 为当前线程存储场景信息
     *
     * @param commonScenario 场景对象
     */
    public static void set(CommonScenario commonScenario) {
        scenarioThreadLocal.set(commonScenario);
    }

    /**
     * 清除线程变量
     */
    public static void clear() {
        scenarioThreadLocal.remove();
    }
}
