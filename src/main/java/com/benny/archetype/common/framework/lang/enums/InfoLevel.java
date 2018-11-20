package com.benny.archetype.common.framework.lang.enums;

/**
 * @author yin.beibei
 * @date 2018/11/20 9:54
 */
public enum InfoLevel implements BaseEnum<InfoLevel> {

    /** 信息 */
    INFO("1", "信息"),

    /** 警告 */
    WARN("3", "警告"),

    /** 异常 */
    ERROR("5", "异常"),

    /** 毁灭性错误 */
    FATAL("7", "毁灭性错误")
    ;


    /** 等级码 */
    private String code;

    /** 等级描述 */
    private String description;

    /**
     * 构造方法
     * @param code          等级码
     * @param description   等级描述
     */
    InfoLevel(String code, String description)
    {
        this.code = code;
        this.description = description;
    }

    /**
     * @see BaseEnum#getCode()
     */
    public String getCode() {
        return code;
    }

    /**
     * @see BaseEnum#getDescription()
     */
    public String getDescription()
    {
        return description;
    }

}
