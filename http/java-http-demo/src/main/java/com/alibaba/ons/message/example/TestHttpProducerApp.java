package com.alibaba.ons.message.example;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.ons.message.example.producer.HttpMQProducer;

import java.util.Date;

public class TestHttpProducerApp {

	/**
	 * 启动测试之前请修改配置文件:producer/producer.xml
	 */
	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("producer/producer.xml");
		HttpMQProducer producer = context.getBean(HttpMQProducer.class);

		// 发送定时消息: producer.send("msg", "tag", "key", startDeliverTime);
		for(int i=0;i<1000;i++){
			String msg = "simple test "+i+" http message,"+new Date();
			if (producer.send(msg, "tag", "key")) {
				System.out.println(msg+" result: success ");
			} else {
				System.out.println(msg+" result: failed ");
			}
		}


		context.close();
	}

}
