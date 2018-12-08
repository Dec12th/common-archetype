package com.benny.framework.common.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author yin.beibei
 * @date 2018/11/29 13:41
 */
@Configuration
@Scan(basePackages = "com.benny.framework.common.test")
public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
//        annotationConfigApplicationContext.register(BeanScannerConfigurer.class);
        annotationConfigApplicationContext.scan(Test.class.getPackage().getName());
        annotationConfigApplicationContext.refresh();
        User user = annotationConfigApplicationContext.getBean(User.class);
        System.out.println(user.hello());
    }

    @MethodProxy
    public void methodProxy() {

    }
}
