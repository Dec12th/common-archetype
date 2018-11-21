package com.benny.framework.common.framework.config.ex;

/**
 * @author yin.beibei
 * @date 2018/11/20 10:12
 */
public class PropertyTypeConvertorNotExistsException extends RuntimeException {
    /** serialVersionUID */
    private static final long serialVersionUID = 724742002295474170L;

    /**
     * 构造函数
     * @param clazz 转换器对应的类型
     */
    public PropertyTypeConvertorNotExistsException(Class clazz)
    {
        super(buildMessage(clazz));
    }

    /**
     * 构建异常消息
     * @param clazz 转换器对应的类型
     * @return 异常消息
     */
    private static String buildMessage(Class clazz)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("找不到该类的转换器: ").append(clazz.getName());
        return stringBuilder.toString();
    }

}
