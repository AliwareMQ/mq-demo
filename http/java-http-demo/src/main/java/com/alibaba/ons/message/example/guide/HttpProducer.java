package com.alibaba.ons.message.example.guide;

import java.util.Date;
import java.util.Properties;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
public class HttpProducer {
    public static String SIGNATURE="Signature";
    public static String NUM="num";
    public static String CONSUMERID="ConsumerID";
    public static String PRODUCERID="ProducerID";
    public static String TIMEOUT="timeout";
    public static String TOPIC="Topic";
    public static String AK="AccessKey";
    public static String BODY="body";
    public static String MSGHANDLE="msgHandle";
    public static String TIME="time";
    public static void main(String[] args) throws Exception {
        HttpClient httpClient=new HttpClient();
        httpClient.setMaxConnectionsPerDestination(1);
        httpClient.start();
        Properties properties=new Properties();
        properties.load(HttpProducer.class.getClassLoader().getResourceAsStream("user.properties"));
        String topic=properties.getProperty("Topic"); //请在user.properties配置您的Topic
        String url=properties.getProperty("URL");//公测集群配置为http://publictest-rest.ons.aliyun.com/
        String ak=properties.getProperty("Ak");//请在user.properties配置您的Ak
        String sk=properties.getProperty("Sk");//请在user.properties配置您的Sk
        String pid=properties.getProperty("ProducerID");//请在user.properties配置您的Producer ID
        String date=String.valueOf(new Date().getTime());
        String sign=null;
        String body="hello ons http";
        String NEWLINE="\n";
        String signString;
        for (int i = 0; i < 10; i++) {
            date=String.valueOf(new Date().getTime());
            Request req=httpClient.POST(url+"?topic="+topic+"&time="+date+"&tag=http"+"&key=http");
            ContentProvider content=new StringContentProvider(body);
            req.content(content);
            sign=SignGuide.postSign(sk,topic,pid,body,date);

            req.header(SIGNATURE, sign);
            req.header(AK, ak);
            req.header(PRODUCERID, pid);
            ContentResponse response;
            response=req.send();
            System.out.println("send msg:"+response.getStatus()+response.getContentAsString());
        }
    }
}
