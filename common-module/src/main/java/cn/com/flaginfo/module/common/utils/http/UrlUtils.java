package cn.com.flaginfo.module.common.utils.http;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author meng.liu
 * @version 1.0
 * @className UrlUtils
 * @describe TODO
 * @date 2019-06-05 14:17
 */
public class UrlUtils {

    private static final Pattern URL_PATTERN = Pattern.compile("^(((ht|f)tps?):\\/\\/)(([^\\/\\:\\?]*)\\:?([0-9]*))[\\/]?([^\\?]*)[\\?]?(.*)");


    public static boolean isUrl(String url) {
        return URL_PATTERN.matcher(url).find();
    }

    public static String protocol(String url) {
        Matcher matcher = URL_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.group(2).trim();
        }
        return null;
    }

    public static String host(String url) {
        Matcher matcher = URL_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.group(5).trim();
        }
        return null;
    }

    public static String port(String url) {
        Matcher matcher = URL_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.group(6).trim();
        }
        return null;
    }

    public static String uri(String url) {
        Matcher matcher = URL_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.group(7).trim();
        }
        return null;
    }


    public static String param(String url) {
        Matcher matcher = URL_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.group(8).trim();
        }
        return null;
    }

    public static Url parse(String urlStr) {
        Url url = new Url();
        Matcher matcher = URL_PATTERN.matcher(urlStr);
        if (matcher.find()) {
            url.isUrl = true;
            url.protocol = matcher.group(2).trim();
            url.host = matcher.group(5).trim();
            String port = matcher.group(6).trim();
            if( StringUtils.isBlank(port) ){
                if( url.protocol.equalsIgnoreCase(HttpConstants.PROTOCOL_HTTPS) ){
                    url.port = HttpConstants.DEFAULT_HTTPS_PORT;
                }else{
                    url.port = HttpConstants.DEFAULT_HTTP_PORT;
                }
            }else {
                url.port = Integer.valueOf(port);
            }

            url.uri = matcher.group(7);
            url.params = matcher.group(8);
        } else {
            url.isUrl = false;
        }
        return url;
    }

    @Getter
    @ToString
    public static class Url {
        private boolean isUrl;
        private String protocol;
        private String host;
        private Integer port;
        private String uri;
        private String params;

        private Url() {
        }

        public String toUrl() {
            StringBuilder urlBuild = new StringBuilder();
            if (StringUtils.isNoneBlank(protocol)) {
                urlBuild.append(protocol).append(HttpConstants.QP_SEP_CSS);
            }
            urlBuild.append(host);
            if (null != port) {
                urlBuild.append(HttpConstants.QP_SEP_C).append(port);
            }
            if (StringUtils.isNoneBlank(uri)) {
                urlBuild.append(HttpConstants.QP_SEP_S).append(uri);
            }
            if (StringUtils.isNotBlank(params)) {
                urlBuild.append(HttpConstants.QP_SEP_P).append(params);
            }
            return urlBuild.toString();
        }

    }
}
