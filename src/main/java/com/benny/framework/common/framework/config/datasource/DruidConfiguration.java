package com.benny.framework.common.framework.config.datasource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yin.beibei
 * @date 2018/11/21 16:12
 */
@Configuration
@EnableConfigurationProperties({DruidProperties.class,DataSourceProperties.class})
@ConditionalOnProperty("spring.datasource.druid.enabled")
public class DruidConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DruidConfiguration.class);

    @Autowired
    DruidProperties druidProperties;
    @Autowired
    DataSourceProperties dataSourceProperties;

    @Bean
    public DataSource druidDataSource() throws SQLException {
        logger.info("druidDataSource init============");

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setName(dataSourceProperties.getName());
        druidDataSource.setUrl(dataSourceProperties.getUrl());
        druidDataSource.setUsername(dataSourceProperties.getUsername());
        druidDataSource.setPassword(dataSourceProperties.getPassword());
        druidDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());

        druidDataSource.setInitialSize(dataSourceProperties.getInitialSize());
        druidDataSource.setMaxActive(dataSourceProperties.getMaxActive());
        druidDataSource.setMinIdle(dataSourceProperties.getMinIdle());
        druidDataSource.setMaxWait(dataSourceProperties.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(dataSourceProperties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(dataSourceProperties.getMinEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(dataSourceProperties.getValidationQuery());
        druidDataSource.setTestWhileIdle(dataSourceProperties.isTestWhileIdle());
        druidDataSource.setTestOnBorrow(dataSourceProperties.isTestOnBorrow());
        druidDataSource.setTestOnReturn(dataSourceProperties.isTestOnReturn());
        druidDataSource.setPoolPreparedStatements(dataSourceProperties.isPoolPreparedStatements());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(dataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        //filters和proxyFilters不能同时设置，否则无法执行批量更新
//		druidDataSource.setFilters(this.filters);
        druidDataSource.setConnectionProperties(dataSourceProperties.getConnectionProperties());
        druidDataSource.setUseGlobalDataSourceStat(dataSourceProperties.isUseGlobalDataSourceStat());
        //支持批量更新
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(wallFilter());
        druidDataSource.setProxyFilters(filters);

        druidDataSource.init();    //初始化
        return druidDataSource;
    }

    /**
     * 注册一个StatViewServlet
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty("druid.config.enabled")
    public ServletRegistrationBean druidStatViewServlet() {
        logger.info("druidStatViewServlet init============");
        //org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), druidProperties.getUrlMapping());

        //添加初始化参数：initParams
        //白名单：
        servletRegistrationBean.addInitParameter("allow", druidProperties.getAllow());
        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        servletRegistrationBean.addInitParameter("deny", druidProperties.getDeny());
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", druidProperties.getLoginUsername());
        servletRegistrationBean.addInitParameter("loginPassword", druidProperties.getLoginPassword());
        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", druidProperties.getResetEnable());
        return servletRegistrationBean;
    }

    /**
     * 注册一个：filterRegistrationBean
     * @return
     */
    @Bean
    @ConditionalOnProperty("druid.config.enabled")
    public FilterRegistrationBean druidStatFilter(){

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());

        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns(druidProperties.getUrlPatterns());

        //添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions",druidProperties.getExclusions());
        filterRegistrationBean.addInitParameter("profileEnable",druidProperties.getProfileEnable());
        return filterRegistrationBean;
    }

    @Bean("wallFilter")
    @ConditionalOnMissingBean(WallFilter.class)
    public WallFilter wallFilter(){
        WallFilter wallFilter=new WallFilter();
        wallFilter.setConfig(wallConfig());
        return  wallFilter;
    }

    @Bean
    @ConditionalOnMissingBean(WallConfig.class)
    public WallConfig wallConfig(){
        WallConfig config =new WallConfig();
        //允许一次执行多条语句
        config.setMultiStatementAllow(true);
        //允许非基本语句的其他语句
        config.setNoneBaseStatementAllow(true);
        return config;
    }
}
