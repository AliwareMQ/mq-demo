# 阿里云消息队列（Message Queue）使用示例

[消息队列（Message Queue，简称MQ）](https://www.aliyun.com/product/ons/)是企业级互联网架构的核心产品，服务于整个阿里巴巴集团已超过 10 年，经过阿里巴巴交易核心链路反复打磨与历年双十一购物狂欢节的严苛考验，是一个真正具备低延迟、高并发、高可用、高可靠，可支撑万亿级数据洪峰的分布式消息中间件。

MQ 的内核为由阿里巴巴集团捐赠给Apache基金会的顶级项目RocketMQ，在2016年双11全球狂欢节中，RocketMQ以万亿级的消息总量支撑了全集团3000+应用，为复杂的业务场景提供系统解耦、削峰填谷能力，保障了核心交易链路消息流转的低延迟、高吞吐。

## 如何开通 MQ 产品

打开 MQ 的[产品页](https://www.aliyun.com/product/ons/)，点击 `立即开通` 即可。如下图所示：

![](https://img.alicdn.com/5476e8b07b923/TB1YuImotfJ8KJjy0FeXXXKEXXa)

## 如何使用该示例程序

首先请阅读MQ 的文档[快速入门](https://help.aliyun.com/document_detail/34411.html)。

## Spring

推荐通过 Spring 的方式接入 MQ。1.6.0.Final+的SDK提供包括所有Client类型的Spring Bean，具体的配置在resources目录下：

* common.xml， 提供AK， SK， Topic， PID 以及CID等资源的配置，使用该Demo前请修改。
* producer.xml， 使用普通Producer的配置文件
* order_producer.xml， 使用OrderProducer的配置文件
* transaction_producer.xml， 使用TransactionProducer的配置文件
* consumer.xml， 使用普通Consumer的配置文件
* order_consumer.xml， 使用OrderConsumer的配置文件
* batch_consumer.xml， 使用BatchConsumer的配置文件

## 非Spring

非Spring应用也可以直接使用消息队列的客户端接入，详见[TCP 接入](https://github.com/AliwareMQ/mq-demo/tree/master/tcp/java-tcp-demo)。

