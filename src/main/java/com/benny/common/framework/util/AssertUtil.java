package com.benny.common.framework.util;

import com.alibaba.fastjson.JSON;
import com.benny.common.framework.config.CommonConfig;
import com.benny.common.framework.config.ConfigKeyEnum;
import com.benny.common.framework.constant.Constants;
import com.benny.common.framework.lang.enums.BaseEnum;
import com.benny.common.framework.lang.exception.CommonErrorCode;
import com.benny.common.framework.lang.exception.CommonException;
import com.benny.common.framework.lang.exception.GenericException;
import com.benny.common.net.domain.BaseResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.Collection;

/**
 * 参数校验工具
 * <ul>
 * <li>需要配置文件</li>
 * <li>配置文件放在classpath的config目录下, 文件名为assertUtil.properties</li>
 * <ul>
 * <li>配置文件中有配置项: exception_class_name, code_class_name</li>
 * <li>需要抛出的异常类(需要实现{@link CommonException})名</li>
 * <li>需要抛出的异常码类(需要继承{@link CommonErrorCode})名</li>
 * </ul>
 * </ul>
 *
 * @author yin.beibei
 * @date 2018/11/20 9:48
 */
public final class AssertUtil {
    /**
     * 日志打印工具
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AssertUtil.class);

    /**
     * 异常构造器
     */
    private static Constructor<? extends CommonException> constructor;

    /**
     * 异常堆栈深度, 用于屏蔽不该显示的最前一条异常堆栈
     */
    private static final Integer ERROR_STACK_DEPTH = 5;


    /**
     * 载入配置并初始化校验环境
     */
    static {
        try {
            constructor = GenericException.class.getConstructor(CommonErrorCode.class, String.class);
            LOGGER.info("初始化默认异常构造器成功.");
        } catch (NoSuchMethodException e) {
            LOGGER.error("默认异常构造器初始化失败...", e);
            throw Constants.UNKNOWN_ERROR;
        }

        String exceptionClassName = null;
        Class<? extends CommonException> exceptionClass;

        try {
            exceptionClassName = CommonConfig.getProperty(ConfigKeyEnum.ASSERTUTIL_EXCEPTION_CLASS_NAME);
            if (StringUtils.isNotBlank(exceptionClassName)) {
                exceptionClass = (Class<? extends CommonException>) Class.forName(exceptionClassName);
                LOGGER.info("载入自定义异常类: {} 成功", exceptionClassName);
            } else {
                LOGGER.error("载入自定义异常类名失败, 将使用默认异常类", exceptionClassName);
                exceptionClass = GenericException.class;
            }


            Constructor[] exceptionConstructors = exceptionClass.getConstructors();
            boolean foundConstructor = false;
            for (Constructor exceptionConstructor : exceptionConstructors) {
                Class[] parameterTypes = exceptionConstructor.getParameterTypes();
                if (parameterTypes != null && parameterTypes.length == 2) {
                    if (CommonErrorCode.class.isAssignableFrom(parameterTypes[0]) && String.class == parameterTypes[1]) {
                        constructor = exceptionConstructor;
                        foundConstructor = true;
                    }
                }
            }
            if (foundConstructor) {
                LOGGER.info("初始化异常构造器成功.");
            } else {
                LOGGER.warn("初始化异常构造器失败, 将使用默认异常构造器");
            }
        } catch (ClassNotFoundException e) {
            LOGGER.error("没有找到异常定义类: {}, 将使用默认异常定义类 \n异常信息: {}.", exceptionClassName, e.getMessage());
        }
    }

    /**
     * 检查BaseResult执行是否成功
     * <li>成功-通过</li>
     * <li>不成功-抛出异常</li>
     *
     * @param baseResult      待检测对象
     * @param commonErrorCode 待抛出错误码
     * @param messages        错误信息
     */
    public static void isResultSuccess(BaseResult baseResult, CommonErrorCode commonErrorCode, Object... messages) {
        isNotNull(baseResult, CommonErrorCode.UNKNOWN_ERROR, "结果对象为null");
        String errorContextInfo = "";
        if (baseResult.getErrorContext() != null) {
            errorContextInfo = JSON.toJSONString(baseResult.getErrorContext());
        }

        if (messages == null || messages.length < 1) {
            messages = new Object[]{errorContextInfo};
        } else {
            String localMessage = generateMessage(messages);
            StringBuilder stringBuilder = new StringBuilder(localMessage);
            stringBuilder.append("\t异常信息: ")
                         .append(errorContextInfo);
            messages = new Object[]{stringBuilder.toString()};
        }

        isTrue(baseResult.isSuccess(), commonErrorCode, messages);
    }

    /**
     * 检查参数是否为true
     * <li>为true-通过</li>
     * <li>不为true-抛出异常</li>
     *
     * @param value           待检测参数
     * @param commonErrorCode 待抛出错误码
     * @param messages        错误信息
     */
    public static void isTrue(boolean value, CommonErrorCode commonErrorCode, Object... messages) {
        if (!value) {
            String extraMessage;

            if (messages.length == 0) {
                extraMessage = null;
            } else if (messages.length == 1) {
                if (messages[0] == null) {
                    extraMessage = null;
                } else {
                    extraMessage = messages[0].toString();
                }
            } else {
                extraMessage = generateMessage(messages);
            }


            CommonException exception;

            try {
                exception = constructor.newInstance(commonErrorCode, extraMessage);
                regularStackTraceElements(exception);
            } catch (Exception e) {
                LOGGER.error("创建异常对象错误", e);
                throw new RuntimeException(e);
            }

            // 该异常处理工具只处理实现了 CommonException 类的异常
            throw (RuntimeException) exception;
        }
    }

    /**
     * 生成额外信息, messages大小必须大于等于1
     *
     * @param messages 额外信息, [0]为pattern, [1-lastIndex]为pattern参数
     * @return 额外信息
     */
    private static String generateMessage(Object[] messages) {
        if (messages == null || messages.length == 0) {
            return null;
        }

        if (messages.length == 1) {
            return JSON.toJSONString(messages[0]);
        }

        String messagePattern = messages[0].toString();
        Object[] messageParams = new Object[messages.length - 1];
        for (int paramIndex = 0; paramIndex < messageParams.length; paramIndex++) {
            messageParams[paramIndex] = JSON.toJSONString(messages[paramIndex + 1]);
        }

        return MessageFormat.format(messagePattern, messageParams);
    }


    /**
     * 检查参数是否为false
     * <li>为false-通过</li>
     * <li>不为false-抛出异常</li>
     *
     * @param value           待检测参数
     * @param commonErrorCode 待抛出错误码
     * @param messages        错误信息
     */
    public static void isFalse(boolean value, CommonErrorCode commonErrorCode, Object... messages) {
        isTrue(!value, commonErrorCode, messages);
    }

    /**
     * 检查字符串是否是数字
     *
     * @param value           字符串
     * @param commonErrorCode 异常码
     * @param messages        异常信息
     */
    public static void isNumber(String value, CommonErrorCode commonErrorCode, Object... messages) {
        isTrue(StringUtils.isNumeric(value), commonErrorCode, messages);
    }

    /**
     * 检查字符串参数是否有值
     * <ul>
     * <li>null或""-通过</li>
     * <li>有值-抛参数异常</li>
     * </ul>
     *
     * @param value           字符串
     * @param commonErrorCode 异常码
     * @param messages        异常信息
     */
    public static void isBlank(String value, CommonErrorCode commonErrorCode, Object... messages) {
        isTrue(StringUtils.isBlank(value), commonErrorCode, messages);
    }

    /**
     * 检查字符串参数是否无值
     * <ul>
     * <li>有值-通过</li>
     * <li>null或""-抛参数异常</li>
     * </ul>
     *
     * @param value           字符串
     * @param commonErrorCode 异常码
     * @param messages        异常信息
     */
    public static void isNotBlank(String value, CommonErrorCode commonErrorCode, Object... messages) {
        isTrue(StringUtils.isNotBlank(value), commonErrorCode, messages);
    }

    /**
     * 检查集合对象是否为空
     * <ul>
     * <li>size为0或对象为null-通过</li>
     * <li>非空-抛参数异常</li>
     * </ul>
     *
     * @param value           集合对象
     * @param commonErrorCode 异常码
     * @param messages        异常信息
     */
    public static void isEmpty(Collection value, CommonErrorCode commonErrorCode, Object... messages) {
        isTrue(CollectionUtils.isEmpty(value), commonErrorCode, messages);
    }

    /**
     * 检查集合对象是否非空
     * <ul>
     * <li>非空-通过</li>
     * <li>size为0或对象为null-抛参数异常</li>
     * </ul>
     *
     * @param value           集合对象
     * @param commonErrorCode 异常码
     * @param messages        异常信息
     */
    public static void isNotEmpty(Collection value, CommonErrorCode commonErrorCode, Object... messages) {
        isTrue(!CollectionUtils.isEmpty(value), commonErrorCode, messages);
    }

    /**
     * 检查对象是否不为null
     * <ul>
     * <li>不为null-通过</li>
     * <li>为null-抛参数异常</li>
     * </ul>
     *
     * @param value           对象
     * @param commonErrorCode 异常码
     * @param messages        异常信息
     */
    public static void isNotNull(Object value, CommonErrorCode commonErrorCode, Object... messages) {
        isTrue(value != null, commonErrorCode, messages);
    }

    /**
     * 检查对象是否为null
     * <ul>
     * <li>为null-通过</li>
     * <li>不为null-抛异常</li>
     * </ul>
     *
     * @param value           对象
     * @param commonErrorCode 异常码
     * @param messages        异常信息
     */
    public static void isNull(Object value, CommonErrorCode commonErrorCode, Object... messages) {
        isTrue(value == null, commonErrorCode, messages);
    }

    /**
     * 检查枚举码是否合法, 该枚举类必须实现 {@link BaseEnum}
     * <ul>
     * <li>合法-通过</li>
     * <li>不合法-抛异常</li>
     * </ul>
     *
     * @param enumCode        枚举码
     * @param enumClass       枚举类型
     * @param commonErrorCode 异常码
     * @param messages        异常信息
     */
    public static void isEnumCodeValid(String enumCode, Class<? extends BaseEnum> enumClass, CommonErrorCode commonErrorCode, Object... messages) {
        isNotNull(BaseEnum.getByCode(enumClass, enumCode), commonErrorCode, messages);
    }

    /**
     * 检查数字是否大于0, 一般来说, 数字类型的id不会小于等于0
     *
     * @param value           数字
     * @param commonErrorCode 异常码
     * @param messages        异常信息
     */
    public static void isGreaterZero(Number value, CommonErrorCode commonErrorCode, Object... messages) {
        isTrue(value.intValue() > 0, commonErrorCode, messages);
    }

    /**
     * 检查集合元素个数是否为指定个数
     * <ul>
     * <li>集合元素个数等于指定个数-通过</li>
     * <li>集合元素个数不等于指定个数-抛异常</li>
     * </ul>
     *
     * @param collection      待检测集合
     * @param reference       参照大小
     * @param commonErrorCode 待抛出错误码
     * @param messages        错误信息
     */
    public static void sizeEq(Collection collection, int reference, CommonErrorCode commonErrorCode, Object... messages) {
        int realSize = calculateCollectionRealSize(collection);
        isTrue(realSize == reference, commonErrorCode, messages);
    }

    /**
     * 检查集合元素个数是否大于指定个数
     * <ul>
     * <li>集合元素个数大于指定个数-通过</li>
     * <li>集合元素个数不大于指定个数-抛异常</li>
     * </ul>
     *
     * @param collection      待检测集合
     * @param reference       参照大小
     * @param commonErrorCode 待抛出错误码
     * @param messages        错误信息
     */
    public static void sizeGreaterThan(Collection collection, int reference, CommonErrorCode commonErrorCode, Object... messages) {
        int realSize = calculateCollectionRealSize(collection);
        isTrue(realSize > reference, commonErrorCode, messages);
    }

    /**
     * 检查集合元素个数是否小于指定个数
     * <ul>
     * <li>集合元素个数小于指定个数-通过</li>
     * <li>集合元素个数不小于指定个数-抛异常</li>
     * </ul>
     *
     * @param collection      待检测集合
     * @param reference       参照大小
     * @param commonErrorCode 待抛出错误码
     * @param messages        错误信息
     */
    public static void sizeLessThan(Collection collection, int reference, CommonErrorCode commonErrorCode, Object... messages) {
        int realSize = calculateCollectionRealSize(collection);
        isTrue(realSize < reference, commonErrorCode, messages);
    }

    /**
     * 使异常堆栈信息规整, 去除不应该显示的最前一条异常堆栈信息
     *
     * @param exception 异常对象
     */
    private static void regularStackTraceElements(CommonException exception) {
        StackTraceElement[] sourceStackTraceElements = exception.getStackTrace();
        StackTraceElement[] targetStackTraceElements = new StackTraceElement[sourceStackTraceElements.length - ERROR_STACK_DEPTH];

        for (int targetElementIndex = 0; targetElementIndex < targetStackTraceElements.length; targetElementIndex++) {
            targetStackTraceElements[targetElementIndex] = sourceStackTraceElements[targetElementIndex + ERROR_STACK_DEPTH];
        }

        exception.setStackTrace(targetStackTraceElements);
    }

    /**
     * 计算集合实际大小, 集合为null时实际大小为0
     *
     * @param collection 集合
     * @return 集合实际大小
     */
    private static int calculateCollectionRealSize(Collection collection) {
        int realSize;
        if (CollectionUtils.isEmpty(collection)) {
            realSize = 0;
        } else {
            realSize = collection.size();
        }
        return realSize;
    }

}
