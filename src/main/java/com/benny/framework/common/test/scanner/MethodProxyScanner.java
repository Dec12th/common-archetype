package com.benny.framework.common.test.scanner;

import com.benny.framework.common.test.annotations.MethodProxy;
import com.benny.framework.common.test.handler.MethodProxyInterceptor;
import com.benny.framework.common.test.proxyfactory.InterfaceProxyFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Set;

/**
 * @ClassName: MethodProxyScanner
 * @Description:
 * @author:大贝
 * @date:2018年12月08日 21:09
 */
public class MethodProxyScanner extends ClassPathBeanDefinitionScanner {

    public MethodProxyScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public void registerDefaultFilters() {
        this.addIncludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                return metadataReader.getAnnotationMetadata().hasAnnotatedMethods(MethodProxy.class.getName());
            }
        });
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
            definition.getPropertyValues().add("innerClassName", definition.getBeanClassName());
            definition.getPropertyValues().add("methodInterceptor", new MethodProxyInterceptor());
            definition.setBeanClass(InterfaceProxyFactoryBean.class);
        }
        return beanDefinitions;
    }

    @Override
    public boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        boolean hasAnnotatedMethods = beanDefinition.getMetadata()
                .hasAnnotatedMethods(MethodProxy.class.getName());
        return hasAnnotatedMethods;
    }

    /**
     * 重写此方法，为了能够代理已经在spirng容器声明中的bean
     * @param beanName
     * @param beanDefinition
     * @return
     * @throws IllegalStateException
     */
    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
        return true;
    }


}
