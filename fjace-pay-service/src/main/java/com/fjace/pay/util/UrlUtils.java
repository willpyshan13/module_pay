package com.fjace.pay.util;

import cn.hutool.core.net.url.UrlBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author fjace
 * @version 1.0.0
 * @Description url 工具
 * @date 2021-09-10 21:29:00
 */
public class UrlUtils {

    /**
     * 拼接url参数
     **/
    public static String appendUrlQuery(String url, Map<String, Object> map) {
        if (StringUtils.isEmpty(url) || map == null || map.isEmpty()) {
            return url;
        }
        UrlBuilder result = UrlBuilder.of(url);
        map.forEach((k, v) -> {
            if (k != null && v != null) {
                result.addQuery(k, v.toString());
            }
        });
        return result.build();
    }

}
