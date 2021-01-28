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
package com.aliyun.openservices.tcp.example.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.batch.BatchConsumer;
import com.aliyun.openservices.ons.api.batch.BatchMessageListener;
import com.aliyun.openservices.tcp.example.MqConfig;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * MQ 接收消息示例 Demo
 */
public class TimedBatchMQConsumer {

    public static void main(String[] args) {
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty(PropertyKeyConst.GROUP_ID, MqConfig.GROUP_ID);
        consumerProperties.setProperty(PropertyKeyConst.AccessKey, MqConfig.ACCESS_KEY);
        consumerProperties.setProperty(PropertyKeyConst.SecretKey, MqConfig.SECRET_KEY);
        consumerProperties.setProperty(PropertyKeyConst.NAMESRV_ADDR, MqConfig.NAMESRV_ADDR);
        consumerProperties.setProperty(PropertyKeyConst.InstanceName, "batch_consumer_demo");
        // Wait until size of message batch grows to 20
        consumerProperties.setProperty(PropertyKeyConst.ConsumeMessageBatchMaxSize, "20");
        // Await at most 10s before firing consumption of message batch
        consumerProperties.setProperty(PropertyKeyConst.BatchConsumeMaxAwaitDurationInSeconds, "10");
        BatchConsumer consumer = ONSFactory.createBatchConsumer(consumerProperties);

        final CountDownLatch latch = new CountDownLatch(3);

        consumer.subscribe(MqConfig.TOPIC, MqConfig.TAG, new BatchMessageListener() {
            @Override
            public Action consume(List<Message> list, ConsumeContext context) {
                System.out.println("Received and committed " + list.size() + " messages.");
                latch.countDown();
                return Action.CommitMessage;
            }
        });
        consumer.start();
        System.out.println("Consumer start success.");

        try {
            latch.await();
            System.out.println("Should have consumed 3 batch-of-message. Shutting consumer down...");
        } catch (InterruptedException e) {
            System.err.println("Interrupted. Shutting consumer down...");
        }
        consumer.shutdown();
    }
}
