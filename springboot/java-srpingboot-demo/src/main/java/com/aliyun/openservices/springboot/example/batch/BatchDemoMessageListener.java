package com.aliyun.openservices.springboot.example.batch;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.batch.BatchMessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BatchDemoMessageListener implements BatchMessageListener {

    @Override
    public Action consume(final List<Message> messages, final ConsumeContext context) {
        System.out.println("Receive: " + messages.size() + " messages");
        for (Message message : messages) {
            System.out.println(message);
        }
        try {
            //do something..
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}
