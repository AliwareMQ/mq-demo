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
package com.alibaba.ons.message.example.consumer;

import com.alibaba.ons.message.example.MqConfig;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import java.util.Properties;

/**
 * MQ 接收消息示例 Demo
 */
public class SimpleOrderConsumer {

    public static void main(String[] args) {
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty(PropertyKeyConst.ConsumerId, MqConfig.ORDER_CONSUMER_ID);
        consumerProperties.setProperty(PropertyKeyConst.AccessKey, MqConfig.ACCESS_KEY);
        consumerProperties.setProperty(PropertyKeyConst.SecretKey, MqConfig.SECRET_KEY);
        consumerProperties.setProperty(PropertyKeyConst.ONSAddr, MqConfig.ONSADDR);
        OrderConsumer consumer = ONSFactory.createOrderedConsumer(consumerProperties);
        consumer.subscribe(MqConfig.ORDER_TOPIC, MqConfig.TAG,  new MessageOrderListener() {

            @Override
            public OrderAction consume(final Message message, final ConsumeOrderContext context) {
                System.out.println(message);
                return OrderAction.Success;
            }
        });
        consumer.start();
        System.out.println("Consumer start success.");

        //等待固定时间防止进程退出
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
