package com.benny.archetype.common.net.log;

import com.benny.archetype.common.framework.lang.enums.BaseEnum;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:22
 */
public enum LoggerTypeEnum implements BaseEnum<LoggerTypeEnum> {
    /**
     * 服务端
     */
    SERVER("S", "服务端"),

    /**
     * 客户端
     */
    CLIENT("C", "客户端");

    /**
     * 类别码
     */
    private String code;

    /**
     * 类别描述
     */
    private String description;

    /**
     * 构造函数
     *
     * @param code        类别码
     * @param description 类别描述
     */
    private LoggerTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static void main(String[] args) {
        System.out.println(LoggerTypeEnum.class.isEnum());
    }
}
