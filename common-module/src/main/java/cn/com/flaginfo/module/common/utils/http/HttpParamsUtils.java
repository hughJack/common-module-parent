package cn.com.flaginfo.module.common.utils.http;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author: Meng.Liu
 * @date: 2019/4/25 下午1:51
 */
public class HttpParamsUtils {

    public static String appendUrlParams(String url, String params){
        if (StringUtils.isBlank(url) || StringUtils.isBlank(params)) {
            return url;
        }
        return stringBuilder(url).append(params).toString();
    }

    public static String appendUrlParams(Map<String, String> params){
        if (CollectionUtils.isEmpty(params)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        params.forEach((key, value) -> builder.append(key)
                .append(HttpConstants.NAME_VALUE_SEPARATOR)
                .append(value).append(HttpConstants.QP_SEP_A));
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    public static String appendUrlParams(String url, Map<String, String> params){
        if (StringUtils.isBlank(url) || CollectionUtils.isEmpty(params)) {
            return url;
        }
        StringBuilder builder = stringBuilder(url);
        params.forEach((key, value) -> builder.append(key)
                .append(HttpConstants.NAME_VALUE_SEPARATOR)
                .append(value).append(HttpConstants.QP_SEP_A));
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    private static StringBuilder stringBuilder(String url){
        StringBuilder builder = new StringBuilder(url);
        if (url.contains(HttpConstants.QP_SEP_P)) {
            builder.append(HttpConstants.QP_SEP_A);
        } else {
            builder.append(HttpConstants.QP_SEP_P);
        }
        return builder;
    }

}
