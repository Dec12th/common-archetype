package com.benny.archetype.common.framework.lang.exception;

import com.benny.archetype.common.framework.constant.Constants;
import com.benny.archetype.common.framework.lang.enums.BaseEnum;
import com.benny.archetype.common.framework.lang.enums.InfoLevel;
import com.benny.archetype.common.framework.lang.enums.InfoType;

/**
 * @author yin.beibei
 * @date 2018/11/20 9:50
 */
public interface CommonErrorCode<T extends CommonErrorCode> extends BaseEnum<T> {
    /**
     * 通用系统异常
     */
    CommonErrorCode UNKNOWN_ERROR = DefaultErrorCodeEnum.UNKNOWN_ERROR;

    /**
     * 通用参数异常
     */
    CommonErrorCode PARAM_ILLEGAL = DefaultErrorCodeEnum.PARAM_ILLEGAL;

    /**
     * 获取信息类型
     */
    InfoType getInfoType();

    /**
     * 获取信息等级
     */
    InfoLevel getInfoLevel();

}

/**
 * 默认异常码枚举
 */
enum DefaultErrorCodeEnum implements CommonErrorCode<DefaultErrorCodeEnum> {
    /**
     * 未知异常
     */
    UNKNOWN_ERROR(Constants.UNKNOWN_ERROR_CODE, Constants.UNKNOWN_ERROR_DESCRIPTION, InfoType.SYSTEM, InfoLevel.ERROR),

    /**
     * 参数异常
     */
    PARAM_ILLEGAL(Constants.PARAM_ILLEGAL_ERROR_CODE, Constants.PARAM_ILLEGAL_ERROR_DESCRIPTION, InfoType.BIZ, InfoLevel.WARN);

    /**
     * 异常码
     */
    private String code;

    /**
     * 异常描述
     */
    private String description;

    /**
     * 异常类型, {@link InfoType#getCode()}
     */
    private InfoType infoType;

    /**
     * 异常等级, {@link InfoLevel#getCode()}
     */
    private InfoLevel infoLevel;

    /**
     * 构造函数
     *
     * @param code        异常码
     * @param description 异常描述
     * @param infoType    异常类别
     * @param infoLevel   异常等级
     */
    private DefaultErrorCodeEnum(String code, String description, InfoType infoType, InfoLevel infoLevel) {
        this.code = code;
        this.description = description;
        this.infoType = infoType;
        this.infoLevel = infoLevel;
    }

    /**
     * @see CommonErrorCode#getInfoType()
     */
    @Override
    public InfoType getInfoType() {
        return infoType;
    }

    /**
     * @see CommonErrorCode#getInfoLevel()
     */
    @Override
    public InfoLevel getInfoLevel() {
        return infoLevel;
    }

    /**
     * @see CommonErrorCode#getCode()
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * @see CommonErrorCode#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
    }
}
