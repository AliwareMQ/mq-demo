/**
 * Copyright (C) 2010-2016 Alibaba Group Holding Limited
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.ons.message.example.producer;

import com.alibaba.ons.message.example.MqConfig;
import com.aliyun.openservices.ons.api.*;

import java.util.Date;
import java.util.Properties;

/**
 * MQ发送定时消息示例 Demo
 */
public class MQTimerProducer {
    public static void main(String[] args) {
        Properties producerProperties = new Properties();
        producerProperties.setProperty(PropertyKeyConst.ProducerId, MqConfig.PRODUCER_ID);
        producerProperties.setProperty(PropertyKeyConst.AccessKey, MqConfig.ACCESS_KEY);
        producerProperties.setProperty(PropertyKeyConst.SecretKey, MqConfig.SECRET_KEY);
        producerProperties.setProperty(PropertyKeyConst.ONSAddr, MqConfig.ONSADDR);
        Producer producer = ONSFactory.createProducer(producerProperties);
        producer.start();
        System.out.println("Producer Started");

        for (int i = 0; i < 10; i++) {
            Message message = new Message(MqConfig.TOPIC, MqConfig.TAG, "mq send timer message test".getBytes());
            // 延时时间单位为毫秒（ms），指定一个时刻，在这个时刻之后才能被消费，这个例子表示 3秒 后才能被消费
            long delayTime = 3000;
            message.setStartDeliverTime(System.currentTimeMillis() + delayTime);
            SendResult sendResult = producer.send(message);
            if (sendResult != null) {
                System.out.println(new Date() + " Send mq timer message success! Topic is:" + MqConfig.TOPIC + "msgId is: " + sendResult.getMessageId());
            }
        }
    }
}
