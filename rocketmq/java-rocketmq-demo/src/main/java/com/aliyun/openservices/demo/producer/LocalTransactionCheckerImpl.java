package com.aliyun.openservices.demo.producer;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * MQ 发送事务消息本地Check接口实现类
 */
public class LocalTransactionCheckerImpl implements TransactionCheckListener {
    /**
     * 本地事务Checker,详见: https://help.aliyun.com/document_detail/29548.html?spm=5176.doc35104.6.133.pJkthu
     */
    @Override public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
        System.out.println("收到事务消息的回查请求, MsgId: " + msg.getMsgId());
        return LocalTransactionState.COMMIT_MESSAGE;
    }

}
