package com.benny.framework.common.test.register;

import com.benny.framework.common.test.MethodProxy;
import com.benny.framework.common.test.Scanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @ClassName: MethodProxyConfigure
 * @Description:
 * @author:大贝
 * @date:2018年12月08日 20:22
 */
public class MethodProxyRegister extends AbstractRegister<MethodProxy> {

    @Override
    public void registerBean(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(getAnnotationName());
        String[] basePackages = (String[]) annotationAttributes.get("basePackages");
        Scanner scanner = new Scanner(registry);
        scanner.doScan(basePackages);
    }
}
