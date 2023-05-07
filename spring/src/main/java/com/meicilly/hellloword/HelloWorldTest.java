package com.meicilly.hellloword;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 获取bean的方式
 * 1.根据bean的id 但是需要强转 HelloWorld helloWorld = (HelloWorld) ioc.getBean("helloWorld");
 * 2.根据bean的类型获取 如果在IOC容器中有多个该类型的bean则会抛出异常 HelloWorld helloWorld = ioc.getBean(HelloWorld.class);
 * 3.根据bean的id和类型获取  HelloWorld helloWorld = ioc.getBean("helloWorld", HelloWorld.class);
 */
public class HelloWorldTest {
    @Test
    public void testHelloWorld(){
        // TODO: 2023/5/6 创建ioc容器对象
        ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
        // TODO: 2023/5/6 获取ioc容器中的HelloWorld对象
        //HelloWorld helloWorld =(HelloWorld) ioc.getBean("helloWorld");
        //HelloWorld helloWorld = ioc.getBean(HelloWorld.class);
        HelloWorld helloWorld = ioc.getBean("helloWorld", HelloWorld.class);
        // TODO: 2023/5/6 调用sayHello方法
        helloWorld.sayHello();
    }

}
