package com.alibaba.ons.message.example.guide;

import com.alibaba.ons.message.example.util.MD5;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lindezhi on 2017/5/17.
 */
public class SignGuide {

    private static final String NEWLINE="\n";

    private static final String ENCODE = "UTF-8";

    public static final String HmacSHA1 = "HmacSHA1";

    public static String postSign(String sk,String topic,String producerId,String body,String date) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        String signString=topic+NEWLINE+producerId+NEWLINE+ MD5.getInstance().getMD5String(body)+NEWLINE+date;
        return calSignature(signString.getBytes(Charset.forName(ENCODE)), sk);
    }

    public static String getSign(String sk,String topic,String consumerId,String date) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        String signString=topic+NEWLINE+consumerId+NEWLINE+date;
        return calSignature(signString.getBytes(Charset.forName(ENCODE)), sk);
    }

    public static String deleteSign(String sk,String topic,String consumerId,String msgHandle,String date) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        String signString = topic+NEWLINE+consumerId+NEWLINE+msgHandle+NEWLINE+date;
        return calSignature(signString.getBytes(Charset.forName(ENCODE)), sk);
    }

    /**
     * 签名的计算方式,先使用HmacSHA1编码,再使用base64编码
     * java 请使用demo中自带的AuthUtil.calSignature
     * import com.aliyun.openservices.ons.api.impl.authority.AuthUtil;
     */
    public static String calSignature(byte[] data,String key) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        //采用 HmacSHA1 编码
        Mac e = Mac.getInstance(HmacSHA1);
        //key 转成二进制utf8编码
        byte[] keyBytes = key.getBytes(ENCODE);
        e.init(new SecretKeySpec(keyBytes, HmacSHA1));
        //采用 HmacSHA1 计算编码结果
        byte[] sha1EncodedBytes = e.doFinal(data);
        //得到结果后将结果使用base64编码,编码后的结果采用utf8转换为String
        return new String(Base64.encodeBase64(sha1EncodedBytes), ENCODE);
    }

}
