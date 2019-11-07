package com.aliyun.openservices.springboot.example.order;

import com.aliyun.openservices.ons.api.bean.OrderProducerBean;
import com.aliyun.openservices.springboot.example.config.MqConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderProducerClient {

    @Autowired
    private MqConfig mqConfig;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public OrderProducerBean buildOrderProducer() {
        OrderProducerBean orderProducerBean = new OrderProducerBean();
        orderProducerBean.setProperties(mqConfig.getMqPropertie());
        return orderProducerBean;
    }

}
