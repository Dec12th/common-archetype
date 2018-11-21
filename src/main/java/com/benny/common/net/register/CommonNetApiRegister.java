package com.benny.common.net.register;

import com.benny.common.net.annotations.CommonNetApiScan;
import com.benny.common.net.utils.IgnoreApiRequestMappingContext;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * api注册中心
 *
 * @author yin.beibei
 * @date 2018/11/19 14:06
 */
public class CommonNetApiRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    /**
     * 资源加载器
     */
    private ResourceLoader resourceLoader;

    /**
     * 扫描注解类
     */
    private static final Class SCAN_ANNOTATION_CLASS = CommonNetApiScan.class;

    /**
     * 扫描注解类中标注的要扫描的包的字段名
     */
    private static final String BASE_PACKAGES_KEY = "basePackages";

    /**
     * @see ImportBeanDefinitionRegistrar#registerBeanDefinitions(AnnotationMetadata, BeanDefinitionRegistry)
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        if (!annotationMetadata.hasAnnotation(SCAN_ANNOTATION_CLASS.getName())) {
            return;
        }

        String[] basePackages = (String[]) annotationMetadata.getAnnotationAttributes(SCAN_ANNOTATION_CLASS.getName()).get(BASE_PACKAGES_KEY);

        // 添加api包内容至requestMapping排除上下文, 否则api包内的接口会被调用方映射出去
        for (String basePackage : basePackages) {
            IgnoreApiRequestMappingContext.addIgnoredBasePackage(basePackage);
        }


        CommonNetApiScanner commonNetApiScanner = new CommonNetApiScanner(beanDefinitionRegistry);

        // 在 Spring 3.1 需要做这个检查
        if (resourceLoader != null) {
            commonNetApiScanner.setResourceLoader(resourceLoader);
        }

        commonNetApiScanner.registerIncludeFilters();
        commonNetApiScanner.doScan(basePackages);
    }

    /**
     * @see ResourceLoaderAware#setResourceLoader(ResourceLoader)
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
