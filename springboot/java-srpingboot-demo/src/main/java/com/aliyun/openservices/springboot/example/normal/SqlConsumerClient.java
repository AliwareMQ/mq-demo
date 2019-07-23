package com.aliyun.openservices.springboot.example.normal;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.aliyun.openservices.springboot.example.config.MqConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//正式开发时可以加上 @Configuration 注解，这样服务启动时consumer也启动了
//sql92只有mq铂金版才支持
public class SqlConsumerClient {

    @Autowired
    private MqConfig mqConfig;

    @Autowired
    private DemoMessageListener messageListener;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean buildSqlConsumer() {
        ConsumerBean consumerBean = new ConsumerBean();
        //配置文件
        Properties properties = mqConfig.getMqPropertie();
        properties.setProperty(PropertyKeyConst.GROUP_ID, mqConfig.getGroupId());
        consumerBean.setProperties(properties);
        //订阅关系
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<Subscription, MessageListener>();
        Subscription subscription = new Subscription();
        subscription.setTopic(mqConfig.getTopic());
        // 表示需要使用SQL来过滤消息
        subscription.setType("SQL92");
        //需要消息的tag是'TagA'或'TagB'并且自定义属性a(在发送消息的时候通过putUserProperties方法放入)需要在[0,3]
        //SQL过滤同样可以使用消息的tag作为过滤条件(消息的tag在消息的属性中叫做 TAGS)
        //SQL过滤同样可以在顺序消费中使用
        subscription.setExpression("(TAGS is not null and TAGS in ('TagA', 'TagB')) and (a is not null and a between 0 and 3)");
        subscriptionTable.put(subscription, messageListener);
        //订阅多个topic如上面设置

        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;
    }

}
