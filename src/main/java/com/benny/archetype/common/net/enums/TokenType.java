package com.benny.archetype.common.net.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:32
 */
public enum  TokenType {
    /** json web token */
    JWT("JWT", "json web token");

    /** 类型码 */
    private String code;

    /** 类型描述 */
    private String description;

    /**
     * 枚举构造函数
     * @param code
     * @param description
     */
    private TokenType(String code, String description)
    {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据枚举码获取枚举对象
     * @param code 枚举码
     * @return 枚举对象
     */
    public static TokenType getByCode(String code)
    {
        if (StringUtils.isBlank(code))
        {
            return null;
        }

        for (TokenType tokenType : values())
        {
            if (StringUtils.equals(code, tokenType.getCode()))
            {
                return tokenType;
            }
        }

        return null;
    }

    public String getCode()
    {
        return code;
    }

    public String getDescription()
    {
        return description;
    }
}
