package com.benny.common.net.execute.factory.http;

import com.benny.common.net.domain.InterfaceInfo;
import com.benny.common.net.execute.factory.InterfaceProxyFactory;
import com.benny.common.net.execute.handler.ExecuteHandler;
import com.benny.common.net.execute.handler.SpringHttpInterfaceExecuteHandler;
import com.benny.common.net.resolver.InterfaceResolver;
import com.benny.common.net.resolver.SpringHttpInterfaceResolver;
import com.netflix.discovery.EurekaClient;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * @author yin.beibei
 * @date 2018/11/19 11:56
 */
public class SpringHttpInterfaceProxyFactory implements InterfaceProxyFactory
{
    /** 服务发现客户端 */
    private EurekaClient eurekaClient;

    /** okhttp客戶端 */
    private OkHttpClient okHttpClient;

    /** 接口信息解析器 */
    private InterfaceResolver<Class> interfaceResolver = new SpringHttpInterfaceResolver();

    /** 日志记录器 */
    private static final Logger LOGGER = LoggerFactory.getLogger(InterfaceProxyFactory.class);

    /**
     * 构造函数
     * @param okHttpClient    okhttp客戶端
     * @param eurekaClient    服務發現客户端
     */
    public SpringHttpInterfaceProxyFactory(OkHttpClient okHttpClient, EurekaClient eurekaClient)
    {
        this.okHttpClient = okHttpClient;
        this.eurekaClient = eurekaClient;
    }

    /**
     * @see InterfaceProxyFactory#createProxy(Class)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T createProxy(Class<T> interfaceType)
    {
        InterfaceInfo interfaceInfo = interfaceResolver.parse(interfaceType);
        LOGGER.info("{} 接口信息解析成功.", interfaceType.getName());

        ExecuteHandler executeHandler = new SpringHttpInterfaceExecuteHandler(interfaceInfo, okHttpClient, eurekaClient);
        LOGGER.info("{} 远程调用执行器初始化成功.", interfaceInfo.getInterfaceClass().getName());

        return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[]{interfaceType}, executeHandler);
    }
}
