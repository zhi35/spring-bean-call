package com.kuajingnet.springbean.call.example;

import com.kuajingnet.springbean.call.BeanHttpServer;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;


public class Main {

    public static void main(String[] args) throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("demoBean", new DemoBean());
        BeanHttpServer expose = BeanHttpServer.build(beanFactory);
        Thread.currentThread().join();
    }
}
