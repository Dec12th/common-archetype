package com.benny.framework.common.test.register;

import com.benny.framework.common.test.annotations.Scan;
import com.benny.framework.common.test.scanner.MethodProxyScanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @ClassName: MethodProxyConfigure
 * @Description:
 * @author:大贝
 * @date:2018年12月08日 20:22
 */
public class MethodProxyRegister extends AbstractRegister<Scan> {

    @Override
    public void registerBean(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(getAnnotationName());
        String[] basePackages = (String[]) annotationAttributes.get("basePackages");
        MethodProxyScanner scanner = new MethodProxyScanner(registry);
        scanner.doScan(basePackages);
    }
}
