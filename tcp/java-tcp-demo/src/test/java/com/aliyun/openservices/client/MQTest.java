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
package com.aliyun.openservices.client;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import org.junit.Test;

import java.util.Date;
import java.util.Properties;

/**
 * @author jixiang.jjx
 */
public class MQTest {
    /**
     * 启动测试之前请替换如下 XXX 为您的配置
     */
    private static final String TOPIC = "XXX";
    private static final String PRODUCER_ID = "XXX";
    private static final String CONSUMER_ID = "XXX";
    private static final String ACCESS_KEY = "XXX";
    private static final String SECRET_KEY = "XXX";
    private static final String TAG = "mq_test_tag";
    private static final Producer producer;
    private static final TransactionProducer transactionProducer;

    static {
        /**
         * 初始化消息Consumer,如果不期望
         */

        {
            Properties consumerProperties = new Properties();
            consumerProperties.setProperty(PropertyKeyConst.ConsumerId, CONSUMER_ID);
            consumerProperties.setProperty(PropertyKeyConst.AccessKey, ACCESS_KEY);
            consumerProperties.setProperty(PropertyKeyConst.SecretKey, SECRET_KEY);
            consumerProperties.setProperty(PropertyKeyConst.ONSAddr, "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
            Consumer consumer = ONSFactory.createConsumer(consumerProperties);
            consumer.subscribe(TOPIC, TAG, new MessageListener() {
                @Override
                public Action consume(Message message, ConsumeContext consumeContext) {
                    System.out.println(new Date() + "Receive message, Topic is:" + TOPIC + ", MsgId is:" + message.getMsgID());
                    //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
                    return Action.CommitMessage;
                }
            });
            consumer.start();
        }

        /**
         * 初始化事务消息Producer
         */
        {
            Properties tranProducerProperties = new Properties();
            tranProducerProperties.setProperty(PropertyKeyConst.ProducerId, PRODUCER_ID);
            tranProducerProperties.setProperty(PropertyKeyConst.AccessKey, ACCESS_KEY);
            tranProducerProperties.setProperty(PropertyKeyConst.SecretKey, SECRET_KEY);
            tranProducerProperties.setProperty(PropertyKeyConst.ONSAddr, "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
            //初始化事务消息Producer时,需要注册一个本地事务状态的的Checker
            LocalTransactionCheckerImpl localTransactionChecker = new LocalTransactionCheckerImpl();
            transactionProducer = ONSFactory.createTransactionProducer(tranProducerProperties, localTransactionChecker);
            transactionProducer.start();
        }

        /**
         * 初始化消息Producer
         */
        {
            Properties producerProperties = new Properties();
            producerProperties.setProperty(PropertyKeyConst.ProducerId, PRODUCER_ID);
            producerProperties.setProperty(PropertyKeyConst.AccessKey, ACCESS_KEY);
            producerProperties.setProperty(PropertyKeyConst.SecretKey, SECRET_KEY);
            producerProperties.setProperty(PropertyKeyConst.ONSAddr, "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
            producer = ONSFactory.createProducer(producerProperties);
            producer.start();
        }
    }

    /**
     * 发送MQ普通消息
     */
    @Test
    public void testSendMsg() {
        Message msg = new Message(TOPIC, TAG, "mq send message test".getBytes());
        SendResult sendResult = producer.send(msg);
        if (sendResult != null) {
            System.out.println("Send mq message success! Topic is:" + TOPIC + "msgId is: " + sendResult.getMessageId());
        }
    }

    /**
     * 发送MQ普通消息
     */
    @Test
    public void testSendTranMsg() {
        Message msg = new Message(TOPIC, TAG, "mq send transaction message test".getBytes());
        SendResult sendResult = transactionProducer.send(msg, new LocalTransactionExecuter() {
            @Override
            public TransactionStatus execute(Message message, Object o) {
                System.out.println("执行本地事务, 并根据本地事务的状态提交TransactionStatus.");
                // 如需测试事务消息的回调接口,请将TransactionStatus.CommitTransaction 替换成 TransactionStatus.RollbackTransaction
                return TransactionStatus.CommitTransaction;
            }
        }, null);
        if (sendResult != null) {
            System.out.println("Send Transaction message success! Topic is:" + TOPIC + ", msgId is: " + sendResult.getMessageId());
        }
    }

    @Test
    public void testReceiveMsg() throws Exception {
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty(PropertyKeyConst.ConsumerId, CONSUMER_ID);
        consumerProperties.setProperty(PropertyKeyConst.AccessKey, ACCESS_KEY);
        consumerProperties.setProperty(PropertyKeyConst.SecretKey, SECRET_KEY);
        consumerProperties.setProperty(PropertyKeyConst.ONSAddr, "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
        Consumer consumer = ONSFactory.createConsumer(consumerProperties);
        consumer.subscribe(TOPIC, TAG, new MessageListener() {
            @Override
            public Action consume(Message message, ConsumeContext consumeContext) {
                System.out.println(new Date() + "Receive message, Topic is:" + TOPIC + ", MsgId is:" + message.getMsgID());
                //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
                return Action.CommitMessage;
            }
        });
        consumer.start();

        //为了保证消息能正常收到,此处发送10条消息
        for (int i = 0; i < 10; i++) {
            Message msg = new Message(TOPIC, TAG, "mq receive message test".getBytes());
            SendResult sendResult = producer.send(msg);
            System.out.println(new Date() + " Send message success. Topic is:" + TOPIC + ", MsgId is:" + sendResult.getMessageId());
        }
        //如果希望执行时间长一点,可以修改如下的Sleep的数值
        Thread.sleep(30000);
    }

    /**
     * 本地事务Checker,详见: https://help.aliyun.com/document_detail/29548.html?spm=5176.doc35104.6.133.pJkthu
     */
    public static class LocalTransactionCheckerImpl implements LocalTransactionChecker {
        @Override
        public TransactionStatus check(Message message) {
            System.out.println("收到事务消息的回查请求, MsgId: " + message.getMsgID());
            return TransactionStatus.CommitTransaction;
        }
    }
}
