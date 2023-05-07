package com.meicilly.aop.demo;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

// TODO: 2023/5/7 通知类必须配置为Spring管理的bean
@Component
// TODO: 2023/5/7 设置当前类为切面类
@Aspect
public class MyAdvice {
    // TODO: 2023/5/7 设置切入点 @Pointcut注解要求配置在方法上方
    @Pointcut("execution(void com.meicilly.aop.demo.BookDao.update())")
    private void pt(){};
    @Before("pt()")
    public void method(){
        System.out.println(System.currentTimeMillis());
    }

}
