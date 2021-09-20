package com.fjace.pay.core.base.util;

import java.util.List;

/**
 * @author fjace
 * @description 字符串处理工具类
 * @date 2021/9/4 4:13 下午
 **/
public class StringUtils {

    public static final String slash = "/";

    public static String join(String separator, List<String> input) {
        if (input == null || input.size() <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.size(); i++) {
            sb.append(input.get(i));
            // if not the last item
            if (i != input.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static String genUrl(String url, String uri) {
        if (!url.endsWith(slash)) {
            url += slash;
        }
        return url += uri;
    }

    public static Boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return "".equals(str.trim());
    }

}
