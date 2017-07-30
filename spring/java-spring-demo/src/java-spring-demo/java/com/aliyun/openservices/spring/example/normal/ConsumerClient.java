package com.aliyun.openservices.spring.example.normal;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsumerClient {
    public static void main(String[] args) {

        /**
         * 消费者Bean配置在consumer.xml中,可通过ApplicationContext获取或者直接注入到其他类(比如具体的Controller)中.
         */
        ApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
        System.out.println("Consumer Started");
    }
}
