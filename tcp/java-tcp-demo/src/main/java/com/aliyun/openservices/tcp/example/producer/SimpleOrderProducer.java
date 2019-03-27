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
package com.aliyun.openservices.tcp.example.producer;

import java.util.Date;
import java.util.Properties;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.aliyun.openservices.tcp.example.MqConfig;

/**
 * MQ发送普通消息示例 Demo
 */
public class SimpleOrderProducer {


    public static void main(String[] args) {
        Properties producerProperties = new Properties();
        producerProperties.setProperty(PropertyKeyConst.GROUP_ID, MqConfig.ORDER_GROUP_ID);
        producerProperties.setProperty(PropertyKeyConst.AccessKey, MqConfig.ACCESS_KEY);
        producerProperties.setProperty(PropertyKeyConst.SecretKey, MqConfig.SECRET_KEY);
        producerProperties.setProperty(PropertyKeyConst.NAMESRV_ADDR, MqConfig.NAMESRV_ADDR);
        OrderProducer producer = ONSFactory.createOrderProducer(producerProperties);
        producer.start();
        System.out.println("Producer Started");

        for (int i = 0; i < 10; i++) {
            Message msg = new Message(MqConfig.ORDER_TOPIC, MqConfig.TAG, "mq send order message test".getBytes());
            // 设置代表消息的业务关键属性，请尽可能全局唯一。
            String orderId = "biz_" + i % 10;
            msg.setKey(orderId);
            // 分区顺序消息中区分不同分区的关键字段，sharding key于普通消息的key是完全不同的概念。
            // 全局顺序消息，该字段可以设置为任意非空字符串。
            String shardingKey = String.valueOf(orderId);
            try {
                SendResult sendResult = producer.send(msg, shardingKey);
                assert sendResult != null;
                System.out.println(new Date() + " Send mq message success! Topic is:" + MqConfig.ORDER_TOPIC + " msgId is: " + sendResult.getMessageId());
            } catch (ONSClientException e) {
                // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
                System.out.println(new Date() + " Send mq message failed! Topic is:" + MqConfig.TOPIC);
                e.printStackTrace();
            }
        }
    }
}
