package com.benny.archetype.common.framework.lang.enums;


import com.benny.archetype.common.framework.config.CommonConfig;
import com.benny.archetype.common.framework.config.ConfigKeyEnum;
import com.benny.archetype.common.framework.constant.Constants;

/**
 * @author yin.beibei
 * @date 2018/11/19 17:42
 */
public interface CommonScenario<T extends CommonScenario> extends BaseEnum<T> {
    /**
     * 常规非日志打印类场景
     */
    CommonScenario COMMON = DefaultScenarioEnum.COMMON;

    /**
     * 获取系统编号
     *
     * @return 系统编号
     */
    String getAppCode();
}

/**
 * 默认场景枚举
 */
enum DefaultScenarioEnum implements CommonScenario<DefaultScenarioEnum> {
    /**
     * 通用场景
     */
    COMMON(Constants.COMMON_SCENARIO_CODE, Constants.COMMON_SCENARIO_DESCRIPTION);

    /**
     * 场景码
     */
    private String code;

    /**
     * 场景描述
     */
    private String description;

    /**
     * 构造函数
     *
     * @param code        场景码
     * @param description 场景描述
     */
    private DefaultScenarioEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * @see CommonScenario#getAppCode()
     */
    @Override
    public String getAppCode() {
        return CommonConfig.getProperty(ConfigKeyEnum.APP_CODE);
    }

    /**
     * @see CommonScenario#getCode()
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * @see CommonScenario#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
    }
}
