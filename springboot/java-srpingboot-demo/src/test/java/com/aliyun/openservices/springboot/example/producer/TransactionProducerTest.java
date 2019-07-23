package com.aliyun.openservices.springboot.example.producer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.TransactionProducerBean;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.aliyun.openservices.springboot.example.config.MqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionProducerTest {

    //事务消息的Producer 已经注册到了spring容器中，后面需要使用时可以直接注入到其它类中
    @Autowired
    private TransactionProducerBean transactionProducerBean;

    @Autowired
    private MqConfig mqConfig;

    @Test
    public void testSend() {

        Message msg = new Message(mqConfig.getTopic(), "TagA", "Hello MQ".getBytes());
        SendResult sendResult = transactionProducerBean.send(msg, new LocalTransactionExecuter() {
            @Override
            public TransactionStatus execute(Message msg, Object arg) {
                System.out.println("执行本地事务");
                return TransactionStatus.CommitTransaction; //根据本地事务执行结果来返回不同的TransactionStatus
            }
        }, null);
        System.out.println(sendResult);

    }

}
