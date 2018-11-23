package com.benny.framework.common.framework.lang.enums;

import com.benny.framework.common.framework.util.CommonUtil;

/**
 * 花括号
 *
 * @author yin.beibei
 * @date 2018/11/23 15:15
 */
public enum Brace implements BaseEnum<Brace> {
    /**
     * 左花括号
     */
    LEFT_BRACE("001", "{", "左花括号"),

    /**
     * 右花括号
     */
    RIGHT_BRACE("002", "}", "右花括号");

    /**
     * 枚举码
     */
    private String code;

    /**
     * 值
     */
    private String value;

    /**
     * 描述
     */
    private String description;

    /**
     * 私有构造函数
     *
     * @param code        枚举码
     * @param value       枚举值
     * @param description 枚举描述
     */
    private Brace(String code, String value, String description) {
        this.code = code;
        this.value = value;
        this.description = description;
    }

    /**
     * method for get value
     */
    public String getValue() {
        return value;
    }

    /**
     * method for get description
     */
    public String getDescription() {
        return description;
    }

    /**
     * method for get code
     */
    public String getCode() {
        return code;
    }

    /**
     * 根据枚举码获取枚举对象
     *
     * @param code 枚举码
     * @return 枚举对象
     */
    public static Brace getByCode(String code) {
        return CommonUtil.getByCode(code, Brace.class);
    }

}
