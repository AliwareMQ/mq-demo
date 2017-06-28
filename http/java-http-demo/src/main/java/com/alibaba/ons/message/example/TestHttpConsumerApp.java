package com.alibaba.ons.message.example;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.ons.message.example.consumer.HttpMQConsumer;
import com.alibaba.ons.message.example.consumer.SimpleMessage;

public class TestHttpConsumerApp {

	/**
	 * 启动测试之前请修改配置文件:consumer/consumer.xml
	 */
	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer/consumer.xml");
		System.out.println("started-----------");
		HttpMQConsumer consumer = context.getBean(HttpMQConsumer.class);

		System.out.println("---------start   pull message-----------"+new Date());
		for(int i=0;i<1000;i++){
			List<SimpleMessage> list = consumer.pull();
			System.out.println("---------end pull message-----------"+new Date()+" size:"+list.size());
			if (list != null && list.size() > 0) {
				for (SimpleMessage simpleMessage : list) {
					System.out.println(JSON.toJSONString(simpleMessage));
					// 当消息处理成功后，需要进行delete，如果不及时delete将会导致重复消费此消息
					String msgHandle = simpleMessage.getMsgHandle();
					if (consumer.delete(msgHandle)) {
						System.out.println("delete success: " + msgHandle);
					} else {
						System.out.println("delete failed: " + msgHandle);
					}
				}
			}
		}
		Thread.currentThread().sleep(200);
		context.close();
	}

}
