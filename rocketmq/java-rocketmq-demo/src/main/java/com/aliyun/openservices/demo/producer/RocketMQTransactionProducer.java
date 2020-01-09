/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aliyun.openservices.demo.producer;

import com.aliyun.openservices.demo.MqConfig;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class RocketMQTransactionProducer {
    private static RPCHook getAclRPCHook() {
        return new AclClientRPCHook(new SessionCredentials(MqConfig.ACCESS_KEY, MqConfig.SECRET_KEY));
    }

    public static void main(String[] args) throws MQClientException {
        /**
         * 创建事务消息Producer
         */
        TransactionMQProducer transactionMQProducer = new TransactionMQProducer(MqConfig.GROUP_ID, getAclRPCHook());
        transactionMQProducer.setNamesrvAddr(MqConfig.NAMESRV_ADDR);
        transactionMQProducer.setTransactionCheckListener(new LocalTransactionCheckerImpl());
        transactionMQProducer.start();

        for (int i = 0; i < 10; i++) {
            try {
                Message message = new Message(MqConfig.TOPIC,
                    MqConfig.TAG,
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = transactionMQProducer.sendMessageInTransaction(message, new LocalTransactionExecuter() {
                    @Override public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
                        System.out.println("开始执行本地事务： " + msg);
                        return LocalTransactionState.UNKNOW;
                    }
                }, null);
                assert sendResult != null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
