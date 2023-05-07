package com.meicilly.core;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BookTest {
    public static void main(String[] args) {
        // TODO: 2023/5/6 获取IOC容器对象
        ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContextCore.xml");
        //Book book = ioc.getBean("book1", Book.class);
//        Book book = ioc.getBean("book5", Book.class);
//        book.bookAll();
        //cartItem cartItem = ioc.getBean("cartItem3", cartItem.class);
        //cartItem.getBook();
        Book book = ioc.getBean("myFactoryBean", Book.class);
        book.bookAll();
    }
}
