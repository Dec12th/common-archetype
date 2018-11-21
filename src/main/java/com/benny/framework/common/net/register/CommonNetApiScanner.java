package com.benny.framework.common.net.register;

import com.benny.framework.common.net.annotations.CommonNet;
import com.benny.framework.common.net.execute.factory.InterfaceProxyFactoryBean;
import com.benny.framework.common.net.utils.RemoteAppsContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Set;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:09
 */
public class CommonNetApiScanner extends ClassPathBeanDefinitionScanner {
    /** 接口类型字段名, {@link InterfaceProxyFactoryBean#interfaceType} */
    private static final String INTERFACE_TYPE_FIELD_NAME = "interfaceType";

    /** 日志记录器 */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonNetApiScanner.class);

    /**
     * 构造函数, 不使用默认的过滤器, {@link org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider#registerDefaultFilters}
     * @param registry bean定义注册中心
     */
    public CommonNetApiScanner(BeanDefinitionRegistry registry)
    {
        super(registry, false);
    }

    /**
     * @see ClassPathBeanDefinitionScanner#doScan(String...)
     */
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages)
    {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        if (!CollectionUtils.isEmpty(beanDefinitionHolders))
        {
            registerRemoteApps(beanDefinitionHolders);
            processBeanDefinitions(beanDefinitionHolders);
        }

        return beanDefinitionHolders;
    }

    /**
     * @see ClassPathBeanDefinitionScanner#isCandidateComponent(AnnotatedBeanDefinition)
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface()
                && beanDefinition.getMetadata().isIndependent()
                && beanDefinition.getMetadata().hasAnnotation(CommonNet.class.getName());
    }

    /**
     * 注册包含过滤器, 筛选出需要包含的被扫描对象<br/>
     * 只包含接口类
     */
    public void registerIncludeFilters()
    {
        addIncludeFilter(new TypeFilter()
        {
            /**
             * @see TypeFilter#match(MetadataReader, MetadataReaderFactory)
             */
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException
            {
                if (!metadataReader.getClassMetadata().isInterface())
                {
                    return false;
                }

                if (!metadataReader.getAnnotationMetadata().hasAnnotation(CommonNet.class.getName()))
                {
                    return false;
                }

                return true;
            }
        });
    }

    /**
     * 注册远程app
     * @param beanDefinitionHolders bean定义对象
     */
    private void registerRemoteApps(Set<BeanDefinitionHolder> beanDefinitionHolders)
    {
        GenericBeanDefinition genericBeanDefinition;
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders)
        {
            genericBeanDefinition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            String beanClassName = genericBeanDefinition.getBeanClassName();
            try
            {
                Class interfaceType = Class.forName(beanClassName);
                CommonNet commonNet = (CommonNet) interfaceType.getAnnotation(CommonNet.class);
                if (StringUtils.isNotBlank(commonNet.appName()))
                {
                    RemoteAppsContext.addRemoteApp(commonNet.appName());
                }
            }
            catch (Exception e)
            {
                LOGGER.error("注册远程服务名失败, 无法启动该服务调用客户端的饥饿加载, beanClassName: {}, exception: {}", beanClassName, e);
            }
        }
    }

    /**
     * 定义bean
     * @param beanDefinitions bean定义对象
     */
    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions)
    {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();

            String interfaceTypeName = definition.getBeanClassName();

            // api 对象的类型其实是 InterfaceProxyFactoryBean.class
            definition.getConstructorArgumentValues().addGenericArgumentValue(interfaceTypeName);
            definition.setBeanClass(InterfaceProxyFactoryBean.class);

            definition.getPropertyValues().add(INTERFACE_TYPE_FIELD_NAME, interfaceTypeName);
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        }
    }
}
