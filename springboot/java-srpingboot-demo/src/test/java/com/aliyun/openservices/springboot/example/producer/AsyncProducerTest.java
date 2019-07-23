package com.aliyun.openservices.springboot.example.producer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.aliyun.openservices.springboot.example.config.MqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncProducerTest {

    //普通消息的Producer 已经注册到了spring容器中，后面需要使用时可以直接注入到其它类中
    @Autowired
    private ProducerBean producer;

    @Autowired
    private MqConfig mqConfig;

    @Test
    public void testSend() {

        //对于使用异步接口，建议设置单独的回调处理线程池，拥有更灵活的配置和监控能力。
        //如下构造线程的方式请求队列为无界仅用作示例，有OOM的风险。
        //更合理的构造方式请参考阿里巴巴Java开发手册：https://github.com/alibaba/p3c
        producer.setCallbackExecutor(Executors.newFixedThreadPool(10));

        //循环发送消息
        for (int i = 0; i < 1; i++) {
            Message msg = new Message( //
                    // Message所属的Topic
                    mqConfig.getTopic(),
                    // Message Tag 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在MQ服务器过滤
                    mqConfig.getTag(),
                    // Message Body 可以是任何二进制形式的数据， MQ不做任何干预
                    // 需要Producer与Consumer协商好一致的序列化和反序列化方式
                    "Hello MQ".getBytes());
            // 设置代表消息的业务关键属性，请尽可能全局唯一
            // 以方便您在无法正常收到消息情况下，可通过MQ 控制台查询消息并补发
            // 注意：不设置也不会影响消息正常收发
            msg.setKey("ORDERID_100");
            // 发送消息，只要不抛异常就是成功
            try {
                producer.sendAsync(msg, new SendCallback() {
                    @Override
                    public void onSuccess(final SendResult sendResult) {
                        assert sendResult != null;
                        System.out.println(sendResult);
                    }

                    @Override
                    public void onException(final OnExceptionContext context) {
                        //出现异常意味着发送失败，为了避免消息丢失，建议缓存该消息然后进行重试。
                    }
                });
            } catch (ONSClientException e) {
                System.out.println("发送失败");
                //出现异常意味着发送失败，为了避免消息丢失，建议缓存该消息然后进行重试。
            }
        }
    }

}
