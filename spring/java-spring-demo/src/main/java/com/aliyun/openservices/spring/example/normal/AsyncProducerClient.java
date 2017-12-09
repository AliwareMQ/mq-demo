package com.aliyun.openservices.spring.example.normal;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AsyncProducerClient {
    public static void main(String[] args) throws IOException {
        /**
         * 生产者Bean配置在producer.xml中,可通过ApplicationContext获取或者直接注入到其他类(比如具体的Controller)中.
         */
        ApplicationContext context = new ClassPathXmlApplicationContext("producer.xml");
        Producer producer = (Producer) context.getBean("producer");

        Properties properties = (Properties) context.getBean("commonProperties");
        String topic = properties.getProperty("Topic");


        //对于使用异步接口，建议设置单独的回调处理线程池，拥有更灵活的配置和监控能力。
        //如下构造线程的方式请求队列为无界仅用作示例，有OOM的风险。
        //更合理的构造方式请参考阿里巴巴Java开发手册：https://github.com/alibaba/p3c
        producer.setCallbackExecutor(Executors.newFixedThreadPool(10));

        //循环发送消息
        for (int i = 0; i < 100; i++) {
            Message msg = new Message( //
                // Message所属的Topic
                topic,
                // Message Tag 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在MQ服务器过滤
                "TagA",
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
        producer.shutdown();
    }
}
