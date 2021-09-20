package com.fjace.pay.core.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @date 2021-09-04 16:34:00
 */
public class SignUtils {

    private static final Logger log = LoggerFactory.getLogger(SignUtils.class);

    /**
     * 计算签名摘要
     *
     * @param map 参数Map
     * @param key 商户秘钥
     * @return 签名
     */
    public static String getSign(Map<String, Object> map, String key) {
        String result = getStrSort(map);
        result += "key=" + key;

        if (log.isDebugEnabled()) {
            log.debug("signStr:{}", result);
        }
        result = Objects.requireNonNull(md5(result, StandardCharsets.UTF_8.name())).toUpperCase();
        if (log.isDebugEnabled()) {
            log.debug("signValue:{}", result);
        }
        return result;
    }


    /**
     * 获取签名串
     *
     * @param map 参数
     * @return 签名 url 字符串
     */
    public static String getStrSort(Map<String, Object> map) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (null != entry.getValue() && !"".equals(entry.getValue())) {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        return sb.toString();
    }


    /**
     * MD5加密
     *
     * @param value   带加密字符串
     * @param charset 编码类型
     * @return 加密后字符串
     */
    public static String md5(String value, String charset) {
        MessageDigest md;
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

    /** 是否 http 或 https连接 **/
    public static boolean isAvailableUrl(String url){
        if(StringUtils.isEmpty(url)){
            return false;
        }
        return url.startsWith("http://") ||url.startsWith("https://");
    }


}
