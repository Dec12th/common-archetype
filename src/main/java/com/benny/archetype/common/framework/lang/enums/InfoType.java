package com.benny.archetype.common.framework.lang.enums;

/**
 * @author yin.beibei
 * @date 2018/11/20 9:51
 */
public enum InfoType implements BaseEnum<InfoType>  {
    /** 系统 */
    SYSTEM("0", "系统"),

    /** 业务 */
    BIZ("1", "业务"),

    /** 未知 */
    UNKNOWN("9", "未知"),

    ;


    /** 类型码 */
    private String code;

    /** 类型描述 */
    private String description;

    /**
     * 构造方法
     * @param code          类型码
     * @param description   类型描述
     */
    InfoType(String code, String description)
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
    @Override
    public String getDescription()
    {
        return description;
    }
}
