package com.benny.framework.common.test.register;

import com.benny.framework.common.test.annotations.Scan;
import com.benny.framework.common.test.scanner.Scanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author yin.beibei
 * @date 2018/12/7 10:08
 */
public class ScannerRegister extends AbstractRegister<Scan> {

    @Override
    public void registerBean(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        System.out.println(getAnnotationName());
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(getAnnotationName());
        String[] basePackages = (String[]) annotationAttributes.get("basePackages");
        Scanner scanner = new Scanner(registry);
        scanner.doScan(basePackages);
    }

}
