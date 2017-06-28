package com.alibaba.ons.message.example.producer;

import com.alibaba.ons.message.example.MqConfig;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * MQ 使用Spring发送普通消息
 */
public class MQProducer4Spring {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("producer/producer.xml");
        ProducerBean producer = (ProducerBean) ctx.getBean("producer");
        System.out.println("Producer Started");

        for (int i = 0; i < 10; i++) {
            Message message = new Message(MqConfig.TOPIC, MqConfig.TAG, "mq send message test".getBytes());
            SendResult sendResult = producer.send(message);
            if (sendResult != null) {
                System.out.println(new Date() + " Send mq message success! Topic is:" + MqConfig.TOPIC + "msgId is: " + sendResult.getMessageId());
            }
        }
    }


}
