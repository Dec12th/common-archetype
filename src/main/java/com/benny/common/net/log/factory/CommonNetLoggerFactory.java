package com.benny.common.net.log.factory;

import com.benny.common.net.log.CommonNetLogger;
import com.benny.common.net.log.LoggerTypeEnum;

/**
 * 日志工厂
 * @author yin.beibei
 * @date 2018/11/19 14:21
 */
public interface CommonNetLoggerFactory<T> {
    /**
     * 获取日志对象
     * @param loggerTarget   日志目标
     * @param loggerTypeEnum 日志打印器类别枚举
     * @return 日志对象
     */
    CommonNetLogger getLogger(T loggerTarget, LoggerTypeEnum loggerTypeEnum);
}
