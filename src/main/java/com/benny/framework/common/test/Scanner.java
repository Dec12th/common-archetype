package com.benny.framework.common.test;


import com.benny.framework.common.test.proxyfactory.InterfaceProxyFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * @author yin.beibei
 * @date 2018/12/7 10:10
 */
public class Scanner extends ClassPathBeanDefinitionScanner {

    boolean useDefaultFilters = true;

    public Scanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public Scanner(BeanDefinitionRegistry registry,boolean useDefaultFilters ) {
        super(registry,useDefaultFilters);
    }

    @Override
    public void registerDefaultFilters() {
        this.addIncludeFilter(new AnnotationTypeFilter(CustomComponent.class));
        this.addIncludeFilter(new AnnotationTypeFilter(MethodProxy.class));
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
            definition.getPropertyValues().add("innerClassName", definition.getBeanClassName());
            definition.setBeanClass(InterfaceProxyFactoryBean.class);
        }
        return beanDefinitions;
    }

    @Override
    public boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return super.isCandidateComponent(beanDefinition) && beanDefinition.getMetadata()
                                                                           .hasAnnotation(CustomComponent.class.getName())
                &&beanDefinition.getMetadata()
                .hasAnnotatedMethods(MethodProxy.class.getName());
    }

}
