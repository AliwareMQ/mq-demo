/**
 * Copyright (C) 2010-2016 Alibaba Group Holding Limited
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aliyun.openservices.tcp.example;

/**
 * MQ 配置
 */
public class MqConfig {
    /**
     * 启动测试之前请替换如下 XXX 为您的配置
     */
    /**
     * 设置 Topic 前需要在RocketMQ控制台提前创建。需要注意的是，每个Topic仅支持一种消息类型，不要混用。
     */
    public static final String TOPIC = "XXX";
    /**
     * 设置 GroupID前需要在RocketMQ控制台提前创建ConsumerGroup。
     */
    public static final String GROUP_ID = "XXX";
    /**
     * 设置 Topic 前需要在RocketMQ控制台提前创建。需要注意的是，每个Topic仅支持一种消息类型，不要混用。
     */
    public static final String ORDER_TOPIC = "XXX";
    /**
     * 设置 GroupID前需要在RocketMQ控制台提前创建ConsumerGroup。
     */
    public static final String ORDER_GROUP_ID = "XXX";
    /**
     * 如果使用当前Demo访问阿里云RocketMQ 4.0系列实例，请设置访问的阿里云账号的AccessKeyId。
     * 如果使用当前Demo访问阿里云RocketMQ 5.0系列实例，请设置实例详情页获取的实例用户名，不要设置阿里云账号的AccessKeyId。
     */
    public static final String ACCESS_KEY = "XXX";
    /**
     * 如果使用当前Demo访问阿里云RocketMQ 4.0系列实例，请设置访问的阿里云账号的AccessKeySecret。
     * 如果使用当前Demo访问阿里云RocketMQ 5.0系列实例，请设置实例详情页获取的实例密码，不要设置阿里云账号的AccessKeySecret。
     */
    public static final String SECRET_KEY = "XXX";
    public static final String TAG = "mq_test_tag";

    /**
     * NAMESRV_ADDR, 通过"实例管理--获取接入点信息--TCP协议接入点"获取。
     * 如果使用当前Demo访问阿里云RocketMQ 4.0系列实例，接入点应该是类似这样的格式  http://MQ_INST_XXX:xxx，注意！！！一定要有http协议头
     * 如果使用当前Demo访问阿里云RocketMQ 5.0系列实例，接入点应该是类似这样的格式  rmq-cn-xxx.xx:xxx，注意！！！一定不要自己添加http协议头
     */
    public static final String NAMESRV_ADDR = "XXX";

}
