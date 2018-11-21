package com.benny.framework.common.framework.config.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName: MasterProperties
 * @Description:
 * @author:大贝
 * @date:2018年09月23日 22:46
 */
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class DataSourceProperties {
    /**
     * druid数据库名
     */
    private String name;
    /**
     * 数据库url
     */
    private String url;
    /**
     * 数据库用户名
     */
    private String username;
    /**
     * 数据库密码
     */
    private String password;
    /**
     * 数据库驱动器类
     */
    private String driverClassName;
    /**
     * 配置初始化大小、最小、最大
     */
    private int initialSize;
    /**
     * 线程池最大活动数
     */
    private int maxActive;

    /**
     * 线程池最小闲置数
     */
    private int minIdle;
    /**
     * 配置获取连接等待超时的时间
     */
    private int maxWait;
    /**
     * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
     */
    private long timeBetweenEvictionRunsMillis;
    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     */
    private long minEvictableIdleTimeMillis;
    /**
     * 用来检测连接是否有效的sql，要求是一个查询语句
     */
    private String validationQuery;
    /**
     * 检测连接
     */
    private boolean testWhileIdle;

    private boolean testOnBorrow;

    private boolean testOnReturn;
    /**
     * 是否开启PSCache
     */
    private boolean poolPreparedStatements;
    /**
     * 指定每个连接上PSCache的大小
     */
    private int maxPoolPreparedStatementPerConnectionSize;
    /**
     * 配置监控统计拦截的filters
     */
    private String filters;
    /**
     * 设置慢SQL记录
     */
    private String connectionProperties;
    /**
     * 设置公用监控数据，合并多个DruidDataSource的监控数据
     */
    private boolean useGlobalDataSourceStat;
}
