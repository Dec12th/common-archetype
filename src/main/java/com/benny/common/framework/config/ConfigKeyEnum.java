package com.benny.common.framework.config;

import com.benny.common.framework.config.convertor.PropertyTypeConvertor;
import com.benny.common.framework.lang.enums.BaseEnum;
import com.benny.common.framework.util.AssertUtil;
import com.benny.common.framework.util.ErrorContextBuilder;
import org.apache.commons.lang.StringUtils;

/**
 * @author yin.beibei
 * @date 2018/11/20 9:47
 */
public enum ConfigKeyEnum implements BaseEnum<ConfigKeyEnum> {
    /**
     * 系统编号
     */
    APP_CODE("biz.appCode", "业务线编码", false),

    /**
     * AssertUtil工具使用的异常类名, {@link AssertUtil}
     */
    ASSERTUTIL_EXCEPTION_CLASS_NAME("assertutil.exception.class.name", "AssertUtil工具使用的异常类名", true),

    /**
     * 异常码前缀, {@link ErrorContextBuilder#ERROR_CODE_PREFIX}
     */
    BIZ_ERROR_BUILDER_ERROR_CODE_PREFIX("biz.error.builder.errorcode.prefix", "异常码前缀", true),

    /**
     * 异常码版本号, {@link ErrorContextBuilder#ERROR_CODE_VERSION}
     */
    BIZ_ERROR_BUILDER_ERROR_CODE_VERSION("biz.error.builder.errorcode.version", "异常码版本号", true),

    /**
     * 异常信息构造器堆栈深度, {@link ErrorContextBuilder#ERROR_BUILDER_STACK_DEPTH}
     */
    BIZ_ERROR_BUILDER_STACK_DEPTH("biz.error.builder.stack.depth", Integer.class, "异常信息堆栈深度, 代表屏蔽前几层异常堆栈信息", true),

    /**
     * 异常信息构造器堆栈长度, {@link ErrorContextBuilder#ERROR_BUILDER_STACK_LENGTH}
     */
    BIZ_ERROR_BUILDER_STACK_LENGTH("biz.error.builder.stack.length", Integer.class, "异常信息堆栈长度", true),

    /**
     * 测试使用mock, true会启用mock, false会关闭mock, 用于自测或联调需要
     */
    TEST_USE_MOCK("test.use.mock", Boolean.class, "测试使用mock", true);

    /**
     * 配置名
     */
    private String code;

    /**
     * 配置结构类型
     */
    private Class propertyType;

    /**
     * 配置描述
     */
    private String description;

    /**
     * 是否可为null
     */
    private boolean allowNull;

    /**
     * 构造函数
     *
     * @param code        配置名
     * @param description 配置描述
     * @param allowNull   是否可为null
     */
    private ConfigKeyEnum(String code, String description, boolean allowNull) {
        this.code = code;
        this.propertyType = String.class;
        this.description = description;
        this.allowNull = allowNull;
    }

    /**
     * 构造函数, 使用该构造函数的配置结构类型将被显示指定<br/>
     * 如果自定义配置类型, 需要编写转换器 {@link PropertyTypeConvertor} <br/>
     * 转换器需加载进转换工厂中 {@link CommonConfig#loadPropertyTypeConvertor()} <br/>
     *
     * @param code         配置名
     * @param propertyType 配置结构类型
     * @param description  配置描述
     * @param allowNull    是否可为null
     */
    private ConfigKeyEnum(String code, Class propertyType, String description, boolean allowNull) {
        this.code = code;
        this.propertyType = propertyType;
        this.description = description;
        this.allowNull = allowNull;
    }

    /**
     * 根据配置名找到配置枚举
     *
     * @param code 配置名
     * @return 配置枚举
     */
    public static ConfigKeyEnum getByCode(String code) {
        for (ConfigKeyEnum configKeyEnum : values()) {
            if (StringUtils.equals(configKeyEnum.getCode(), code)) {
                return configKeyEnum;
            }
        }

        return null;
    }

    /**
     * @see BaseEnum#getCode()
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * 获取配置的结构类型
     *
     * @return 配置的结构类型
     */
    @SuppressWarnings("unchecked")
    public <T> Class<T> getPropertyType() {
        return propertyType;
    }

    /**
     * @see BaseEnum#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * 该配置是否允许null值
     *
     * @return 该属性是否允许null值
     */
    public boolean isAllowNull() {
        return allowNull;
    }

}
