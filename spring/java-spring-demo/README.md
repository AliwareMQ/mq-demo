# mq-demo
Spring Demo for AliwareMQ

# Spring配置文件介绍

AliwareMQ 支持以Spring的方式接入，1.6.0.Final+的SDK提供包括所有Client类型的Spring Bean，具体的配置在resources目录下：

* common.xml， 提供AK， SK， Topic， PID 以及CID等资源的配置，使用该Demo前请修改。
* producer.xml， 使用普通Producer的配置文件
* order_producer.xml， 使用OrderProducer的配置文件
* transaction_producer.xml， 使用TransactionProducer的配置文件
* consumer.xml， 使用普通Consumer的配置文件
* order_consumer.xml， 使用OrderConsumer的配置文件
* batch_consumer.xml， 使用BatchConsumer的配置文件
