package com.meicilly.core;

import org.springframework.beans.factory.FactoryBean;

public class MyFactoryBean implements FactoryBean<Book> {
    @Override
    public Book getObject() throws Exception {
        return new Book(7,"生命不息，奋斗不止","罗永浩",11.11,1000);
    }

    @Override
    public Class<?> getObjectType() {
        return Book.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
