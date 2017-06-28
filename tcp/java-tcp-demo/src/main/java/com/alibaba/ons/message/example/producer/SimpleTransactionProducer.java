package com.alibaba.ons.message.example.producer;

import com.alibaba.ons.message.example.MqConfig;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;

import java.util.Properties;

/**
 * MQ 发送事务消息示例 Demo
 */
public class SimpleTransactionProducer {

    public static void main(String[] args) {
        Properties tranProducerProperties = new Properties();
        tranProducerProperties.setProperty(PropertyKeyConst.ProducerId, MqConfig.PRODUCER_ID);
        tranProducerProperties.setProperty(PropertyKeyConst.AccessKey, MqConfig.ACCESS_KEY);
        tranProducerProperties.setProperty(PropertyKeyConst.SecretKey, MqConfig.SECRET_KEY);
        tranProducerProperties.setProperty(PropertyKeyConst.ONSAddr, MqConfig.ONSADDR);
        //初始化事务消息Producer时,需要注册一个本地事务状态的的Checker
        LocalTransactionCheckerImpl localTransactionChecker = new LocalTransactionCheckerImpl();
        TransactionProducer transactionProducer = ONSFactory.createTransactionProducer(tranProducerProperties, localTransactionChecker);
        transactionProducer.start();

        Message message = new Message(MqConfig.TOPIC, MqConfig.TAG, "mq send transaction message test".getBytes());

        for (int i = 0; i < 10; i++) {
            SendResult sendResult = transactionProducer.send(message, new LocalTransactionExecuter() {
                public TransactionStatus execute(Message msg, Object arg) {
                    System.out.println("执行本地事务, 并根据本地事务的状态提交TransactionStatus.");
                    return TransactionStatus.CommitTransaction;
                }
            }, null);
        }

        System.out.println("Send transaction message success.");
    }
}