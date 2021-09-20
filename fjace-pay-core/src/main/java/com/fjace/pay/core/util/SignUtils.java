package com.fjace.pay.core.util;

import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 签名工具类
 * @date 2021-09-05 10:37:00
 */
@Slf4j
public class SignUtils {

    public static byte[] AES_KEY = "4ChT08phkz59hquD795X7w==".getBytes();

    /** 加密 **/
    public static String aesEncode(String str){
        return SecureUtil.aes(SignUtils.AES_KEY).encryptHex(str);
    }

    public static String aesDecode(String str){
        return SecureUtil.aes(SignUtils.AES_KEY).decryptStr(str);
    }


    public static String getSign(Map<String,Object> map, String key){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(null != entry.getValue() && !"".equals(entry.getValue())){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        log.info("signStr:{}", result);
        result = Objects.requireNonNull(md5(result, StandardCharsets.UTF_8.name())).toUpperCase();
        log.info("sign:{}", result);
        return result;
    }

    public static String md5(String value, String charset) {
        MessageDigest md = null;
        try {
            byte[] data = value.getBytes(charset);
            md = MessageDigest.getInstance("MD5");
            byte[] digestData = md.digest(data);
            return toHex(digestData);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toHex(byte[] input) {
        if (input == null) {
            return null;
        }
        StringBuilder output = new StringBuilder(input.length * 2);
        for (byte b : input) {
            int current = b & 0xff;
            if (current < 16) {
                output.append("0");
            }
            output.append(Integer.toString(current, 16));
        }
        return output.toString();
    }
}
