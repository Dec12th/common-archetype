package com.benny.framework.common.net.log.factory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.util.FileSize;
import com.benny.framework.common.net.constants.CommonNetConstants;
import com.benny.framework.common.net.log.CommonNetLogger;
import com.benny.framework.common.net.log.GenericCommonNetLogger;
import com.benny.framework.common.net.log.LoggerTypeEnum;
import com.benny.framework.common.net.log.appender.CommonNetAppender;
import com.benny.framework.common.net.utils.log.LogContextLoader;
import com.benny.framework.common.net.utils.log.PatchedSizeAndTimeBasedRollingPolicy;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static ch.qos.logback.core.util.FileSize.GB_COEFFICIENT;
import static ch.qos.logback.core.util.FileSize.MB_COEFFICIENT;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:39
 */
public class GenericCommonNetLoggerFactory implements CommonNetLoggerFactory<Class> {
    /**
     * 日志输出格式
     */
    private static final String LOG_OUTPUT_PATTERN = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";

    /**
     * 客户端日志打印文件名表达式
     */
    private static final String CLIENT_LOG_FILE_NAME_PATTERN = "logs/rpc/client/%d{yyyy-MM-dd}.%i.log";

    /**
     * 服务端日志打印文件名表达式
     */
    private static final String SERVER_LOG_FILE_NAME_PATTERN = "logs/rpc/server/%d{yyyy-MM-dd}.%i.log";

    /**
     * 最大保存天数
     */
    private static final Integer MAX_SAVE_DAYS = 30;

    /**
     * 单一日志文件最大占用空间, 单位: MB
     */
    private static final Long MAX_PER_FILE_SIZE = 10 * MB_COEFFICIENT;

    /**
     * 日志文件最大占用空间, 单位: GB
     */
    private static final Long MAX_LOG_FILES_SIZE = 3 * GB_COEFFICIENT;

    /**
     * 日志类别:文件名格式映射
     */
    private static final Map<LoggerTypeEnum, String> LOGGER_TYPE_ENUM_FILE_PATTERN_MAP = new HashMap<>();

    /**
     * 日志类别:打印器映射
     */
    private static final Map<LoggerTypeEnum, CommonNetAppender> LOGGER_TYPE_ENUM_APPENDER_MAP = new HashMap<>();

    /**
     * 初始化日志打印器工厂环境
     */
    static {
        LOGGER_TYPE_ENUM_FILE_PATTERN_MAP.put(LoggerTypeEnum.CLIENT, CLIENT_LOG_FILE_NAME_PATTERN);
        LOGGER_TYPE_ENUM_FILE_PATTERN_MAP.put(LoggerTypeEnum.SERVER, SERVER_LOG_FILE_NAME_PATTERN);
    }

    /**
     * @see CommonNetLoggerFactory#getLogger(Object, LoggerTypeEnum)
     */
    @Override
    @SuppressWarnings("unchecked")
    public CommonNetLogger getLogger(Class loggerTarget, LoggerTypeEnum loggerTypeEnum) {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerTarget);

        CommonNetAppender appender = LOGGER_TYPE_ENUM_APPENDER_MAP.get(loggerTypeEnum);
        if (appender == null) {
            synchronized (LOGGER_TYPE_ENUM_APPENDER_MAP) {
                if (LOGGER_TYPE_ENUM_APPENDER_MAP.get(loggerTypeEnum) == null) {
                    appender = buildAppender(loggerTypeEnum);
                    LOGGER_TYPE_ENUM_APPENDER_MAP.put(loggerTypeEnum, appender);
                }
            }
        }

        logger.addAppender(appender);

        logger.setAdditive(false);
        logger.setLevel(Level.INFO);

        return new GenericCommonNetLogger(logger);
    }

    /**
     * 构建日志打印器
     *
     * @param loggerTypeEnum 日志类别
     * @return 日志打印器
     */
    private static CommonNetAppender buildAppender(LoggerTypeEnum loggerTypeEnum) {
        CommonNetAppender appender = new CommonNetAppender();
        PatchedSizeAndTimeBasedRollingPolicy rollingPolicy = new PatchedSizeAndTimeBasedRollingPolicy();
        rollingPolicy.setContext(LogContextLoader.getLoggerContext());
        rollingPolicy.setMaxFileSize(new FileSize(MAX_PER_FILE_SIZE));
        rollingPolicy.setFileNamePattern(LOGGER_TYPE_ENUM_FILE_PATTERN_MAP.get(loggerTypeEnum));
        rollingPolicy.setMaxHistory(MAX_SAVE_DAYS);
        rollingPolicy.setTotalSizeCap(new FileSize(MAX_LOG_FILES_SIZE));
        rollingPolicy.setParent(appender);
        rollingPolicy.start();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(LogContextLoader.getLoggerContext());
        encoder.setCharset(Charset.forName(CommonNetConstants.MESSAGE_CHARSET_NAME));
        encoder.setPattern(LOG_OUTPUT_PATTERN);
        encoder.start();

        appender.setRollingPolicy(rollingPolicy);
        appender.setContext(LogContextLoader.getLoggerContext());
        appender.setEncoder(encoder);
        appender.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            /**
             * @see Runnable#run()
             */
            @Override
            public void run() {
                appender.stop();
                encoder.stop();
                rollingPolicy.stop();
            }
        }));

        return appender;
    }

}
