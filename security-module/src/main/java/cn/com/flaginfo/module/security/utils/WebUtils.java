package cn.com.flaginfo.module.security.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author: Meng.Liu
 * @date: 2019-05-09 11:07
 */
@Slf4j
public class WebUtils {

    public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
    public static final String INCLUDE_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.include.context_path";
    public static final String INCLUDE_SERVLET_PATH_ATTRIBUTE = "javax.servlet.include.servlet_path";
    public static final String INCLUDE_PATH_INFO_ATTRIBUTE = "javax.servlet.include.path_info";
    public static final String INCLUDE_QUERY_STRING_ATTRIBUTE = "javax.servlet.include.query_string";

    public static final String FORWARD_REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
    public static final String FORWARD_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.forward.context_path";
    public static final String FORWARD_SERVLET_PATH_ATTRIBUTE = "javax.servlet.forward.servlet_path";
    public static final String FORWARD_PATH_INFO_ATTRIBUTE = "javax.servlet.forward.path_info";
    public static final String FORWARD_QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";

    public static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";

    public static String getPathWithinApplication(HttpServletRequest request) {
        String contextPath = getContextPath(request);
        String requestUri = getRequestUri(request);
        if (StringUtils.startsWithIgnoreCase(requestUri, contextPath)) {
            String path = requestUri.substring(contextPath.length());
            return (StringUtils.isNoneBlank(path) ? path : "/");
        } else {
            return requestUri;
        }
    }


    public static String getRequestUri(HttpServletRequest request) {
        String uri = (String) request.getAttribute(INCLUDE_REQUEST_URI_ATTRIBUTE);
        if (uri == null) {
            uri = request.getRequestURI();
        }
        return normalize(decodeAndCleanUriString(request, uri));
    }

    public static String normalize(String path) {
        return normalize(path, true);
    }


    private static String normalize(String path, boolean replaceBackSlash) {
        if (path == null){
            return null;
        }
        String normalized = path;

        if (replaceBackSlash && normalized.indexOf('\\') >= 0){
            normalized = normalized.replace('\\', '/');
        }

        if (normalized.equals("/.")){
            return "/";
        }
        if (!normalized.startsWith("/")){
            normalized = "/" + normalized;
        }

        while (true) {
            int index = normalized.indexOf("//");
            if (index < 0){
                break;
            }
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 1);
        }
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0) {
                break;
            }
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 2);
        }
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0){
                break;
            }
            if (index == 0){
                return (null);
            }
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) +
                    normalized.substring(index + 3);
        }
        return (normalized);

    }


    private static String decodeAndCleanUriString(HttpServletRequest request, String uri) {
        uri = decodeRequestString(request, uri);
        int semicolonIndex = uri.indexOf(';');
        return (semicolonIndex != -1 ? uri.substring(0, semicolonIndex) : uri);
    }

    public static String getContextPath(HttpServletRequest request) {
        String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
        if (contextPath == null) {
            contextPath = request.getContextPath();
        }
        contextPath = normalize(decodeRequestString(request, contextPath));
        if ("/".equals(contextPath)) {
            // the normalize method will return a "/" and includes on Jetty, will also be a "/".
            contextPath = "";
        }
        return contextPath;
    }


    @SuppressWarnings({"deprecation"})
    public static String decodeRequestString(HttpServletRequest request, String source) {
        String enc = determineEncoding(request);
        try {
            return URLDecoder.decode(source, enc);
        } catch (UnsupportedEncodingException ex) {
            if (log.isWarnEnabled()) {
                log.warn("Could not decode request string [" + source + "] with encoding '" + enc +
                        "': falling back to platform default encoding; exception message: " + ex.getMessage());
            }
            return URLDecoder.decode(source);
        }
    }

    protected static String determineEncoding(HttpServletRequest request) {
        String enc = request.getCharacterEncoding();
        if (enc == null) {
            enc = DEFAULT_CHARACTER_ENCODING;
        }
        return enc;
    }


    public static HttpServletRequest toHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    public static HttpServletResponse toHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

    public static boolean isTrue(ServletRequest request, String paramName) {
        String value = getCleanParam(request, paramName);
        return value != null &&
                (value.equalsIgnoreCase("true") ||
                        value.equalsIgnoreCase("t") ||
                        value.equalsIgnoreCase("1") ||
                        value.equalsIgnoreCase("enabled") ||
                        value.equalsIgnoreCase("y") ||
                        value.equalsIgnoreCase("yes") ||
                        value.equalsIgnoreCase("on"));
    }

    public static String getCleanParam(ServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        if (StringUtils.isBlank(value)) {
            return null;
        } else {
            return value.trim();
        }
    }
}
