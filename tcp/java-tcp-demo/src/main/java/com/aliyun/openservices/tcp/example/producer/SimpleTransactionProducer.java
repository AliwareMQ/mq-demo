package com.aliyun.openservices.tcp.example.producer;

import java.util.Date;
import java.util.Properties;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.aliyun.openservices.tcp.example.MqConfig;

/**
 * MQ 发送事务消息示例 Demo
 */
public class SimpleTransactionProducer {

    public static void main(String[] args) {
        Properties tranProducerProperties = new Properties();
        tranProducerProperties.setProperty(PropertyKeyConst.GROUP_ID, MqConfig.GROUP_ID);
        tranProducerProperties.setProperty(PropertyKeyConst.AccessKey, MqConfig.ACCESS_KEY);
        tranProducerProperties.setProperty(PropertyKeyConst.SecretKey, MqConfig.SECRET_KEY);
        tranProducerProperties.setProperty(PropertyKeyConst.NAMESRV_ADDR, MqConfig.NAMESRV_ADDR);
        //初始化事务消息Producer时,需要注册一个本地事务状态的的Checker
        LocalTransactionCheckerImpl localTransactionChecker = new LocalTransactionCheckerImpl();
        TransactionProducer transactionProducer = ONSFactory.createTransactionProducer(tranProducerProperties, localTransactionChecker);
        transactionProducer.start();

        Message message = new Message(MqConfig.TOPIC, MqConfig.TAG, "mq send transaction message test".getBytes());

        for (int i = 0; i < 10; i++) {
           try{
               SendResult sendResult = transactionProducer.send(message, new LocalTransactionExecuter() {
                   @Override
                   public TransactionStatus execute(Message msg, Object arg) {
                       System.out.println("执行本地事务, 并根据本地事务的状态提交TransactionStatus.");
                       return TransactionStatus.CommitTransaction;
                   }
               }, null);
               assert sendResult != null;
           }catch (ONSClientException e){
               // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
               System.out.println(new Date() + " Send mq message failed! Topic is:" + MqConfig.TOPIC);
               e.printStackTrace();
           }
        }

        System.out.println("Send transaction message success.");
    }
}