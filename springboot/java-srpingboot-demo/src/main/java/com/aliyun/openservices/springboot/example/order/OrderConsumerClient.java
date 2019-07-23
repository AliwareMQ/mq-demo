package com.aliyun.openservices.springboot.example.order;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.OrderConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.springboot.example.config.MqConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//项目中加上 @Configuration 注解，这样服务启动时consumer也启动了
public class OrderConsumerClient {

    @Autowired
    private MqConfig mqConfig;

    @Autowired
    private OrderDemoMessageListener messageListener;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public OrderConsumerBean buildOrderConsumer() {
        OrderConsumerBean orderConsumerBean = new OrderConsumerBean();
        //配置文件
        Properties properties = mqConfig.getMqPropertie();
        properties.setProperty(PropertyKeyConst.GROUP_ID, mqConfig.getOrderGroupId());
        orderConsumerBean.setProperties(properties);
        //订阅关系
        Map<Subscription, MessageOrderListener> subscriptionTable = new HashMap<Subscription, MessageOrderListener>();
        Subscription subscription = new Subscription();
        subscription.setTopic(mqConfig.getOrderTopic());
        subscription.setExpression(mqConfig.getOrderTag());
        subscriptionTable.put(subscription, messageListener);
        //订阅多个topic如上面设置

        orderConsumerBean.setSubscriptionTable(subscriptionTable);
        return orderConsumerBean;
    }

}
