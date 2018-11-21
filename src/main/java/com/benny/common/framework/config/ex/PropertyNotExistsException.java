package com.benny.common.framework.config.ex;

import com.benny.common.framework.config.ConfigKeyEnum;

/**
 * @author yin.beibei
 * @date 2018/11/20 10:11
 */
public class PropertyNotExistsException extends RuntimeException {
    /**
     * 构造函数
     * @param configKeyEnum 配置枚举
     */
    public PropertyNotExistsException(ConfigKeyEnum configKeyEnum)
    {
        super(buildMessage(configKeyEnum));
    }

    /**
     * 构建异常消息
     * @param configKeyEnum 配置枚举
     * @return 异常消息
     */
    private static String buildMessage(ConfigKeyEnum configKeyEnum)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("找不到配置项: ").append(configKeyEnum.getCode());
        return stringBuilder.toString();
    }

}
