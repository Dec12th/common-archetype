package com.benny.archetype.common.net.utils.log;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.impl.StaticLoggerBinder;

/**
 * 日志上下文获取工具
 * @author yin.beibei
 * @date 2018/11/19 14:44
 */
public class LogContextLoader {
    /**
     * 获取日志上下文
     * @return 日志上下文
     */
    public static LoggerContext getLoggerContext()
    {
        return (LoggerContext) StaticLoggerBinder.getSingleton().getLoggerFactory();
    }
}
