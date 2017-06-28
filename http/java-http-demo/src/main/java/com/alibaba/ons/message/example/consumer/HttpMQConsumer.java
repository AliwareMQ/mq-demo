package com.alibaba.ons.message.example.consumer;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.ons.message.example.util.MD5;
import com.aliyun.openservices.ons.api.impl.authority.AuthUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

public class HttpMQConsumer {

	private String accessKey;
	private String secretKey;
	private String consumerId;
	private String url;
	private String topic;
	private static final Logger log = LogManager.getLogger();

	private static final String NEWLINE = "\n";

	public List<SimpleMessage> pull() {
		List<SimpleMessage> result = null;
		long time = System.currentTimeMillis();
		GetRequest req = Unirest.get(url);
		String signString = topic + NEWLINE + consumerId + NEWLINE + time;
		String sign = AuthUtil.calSignature(signString.getBytes(StandardCharsets.UTF_8), secretKey);
		req.header("Signature", sign);
		req.header("AccessKey", accessKey);
		req.header("ConsumerID", consumerId);
		req.queryString("topic", topic);
		req.queryString("time", time);
		req.queryString("num", 32);

		try {
			HttpResponse<String> res = req.asString();
			if (res.getStatus() == 200) {
				if (res.getBody() != null && !res.getBody().isEmpty()) {
					try {
						result = JSON.parseArray(res.getBody(), SimpleMessage.class);
					} catch (Exception e) {
						log.error("get message error", e);
					}
				}
			}
		} catch (UnirestException e) {
			log.error("get message error", e);
		}
		return result;
	}

	public boolean delete(String msgHandle) {
		long time = System.currentTimeMillis();
		HttpRequestWithBody req = Unirest.delete(url);
		String signString = topic + NEWLINE + consumerId + NEWLINE + msgHandle + NEWLINE
				+ time;
		String sign = AuthUtil.calSignature(signString.getBytes(StandardCharsets.UTF_8), secretKey);
		req.header("Signature", sign);
		req.header("AccessKey", accessKey);
		req.header("ConsumerID", consumerId);
		req.queryString("topic", topic);
		req.queryString("time", time);
		req.queryString("timeout","300000");
		req.queryString("msgHandle", msgHandle);
		try {
			HttpResponse<String> res = req.asString();
			if (res.getStatus() == 204) {
				return true;
			} else {
				log.error("delete message error: {}", msgHandle, res.getBody());
			}
		} catch (UnirestException e) {
			log.error("delete message error: {}", msgHandle, e);
		}
		return false;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

}
