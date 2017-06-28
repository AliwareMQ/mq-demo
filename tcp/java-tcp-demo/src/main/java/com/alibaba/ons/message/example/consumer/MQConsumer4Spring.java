package com.alibaba.ons.message.example.consumer;

import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * MQ 通过Spring方式接收消息示例 Demo
 */
public class MQConsumer4Spring {
    /**
     * 启动测试之前请修改配置文件:resources/consumer/consumer.xml
     */
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("consumer/consumer.xml");
        ConsumerBean consumer = (ConsumerBean) ctx.getBean("consumer");
        System.out.println("Consumer Started");
    }

}
