package com.benny.framework.common.test.register;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;

/**
 * @ClassName: AbstractRegister
 * @Description:
 * @author:大贝
 * @date:2018年12月08日 20:26
 */
public abstract class AbstractRegister<T extends Annotation> implements ImportBeanDefinitionRegistrar,ApplicationContextAware {

    ApplicationContext applicationContext;

    ClassPathBeanDefinitionScanner scanner;

    private final String annotationName = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//        if (!entityClass.getClass().isAnnotation()) {
//            throw new IllegalArgumentException("the class type must be an Annotation");
//        }
        registerBean(importingClassMetadata,registry);
    }

    public abstract void registerBean(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry);

    public void setScanner(ClassPathBeanDefinitionScanner scanner) {
        this.scanner = scanner;
    }

    public String getAnnotationName() {
        return annotationName;
    }
}
