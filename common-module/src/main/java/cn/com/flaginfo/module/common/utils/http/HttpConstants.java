package cn.com.flaginfo.module.common.utils.http;

/**
 * @author: Meng.Liu
 * @date: 2019/4/25 下午1:32
 */
public interface HttpConstants {

    String UTF_8 = "utf-8";

    String HTTP_PROTOCOL = "http://";

    String PROTOCOL_HTTP = "http";

    int DEFAULT_HTTPS_PORT = 443;
    String DEFAULT_HTTPS_PORT_STRING = "443";

    String PROTOCOL_HTTPS = "https";
    int DEFAULT_HTTP_PORT = 80;
    String DEFAULT_HTTP_PORT_STRING = "80";

    String HEAD = "HEAD";
    String POST = "POST";
    String PUT = "PUT";
    String GET = "GET";
    String OPTIONS = "OPTIONS";
    String TRACE = "TRACE";
    String DELETE = "DELETE";
    String PATCH = "PATCH";

    String HEADER_COOKIE = "Cookie";
    String HEADER_COOKIE_IN_REQUEST = "Cookie:";

    String HEADER_CONNECTION = "Connection";

    String CONNECTION_CLOSE = "close";
    String KEEP_ALIVE = "keep-alive";

    String HTTP_1_1 = "HTTP/1.1";

    String HEADER_SET_COOKIE = "set-cookie";
    // Brotli compression not supported yet by HC4 4.5.2 , but to be added
    String ENCODING_BROTLI = "br";
    String ENCODING_DEFLATE = "deflate";
    String ENCODING_GZIP = "gzip";

    String HEADER_CONTENT_DISPOSITION = "Content-Disposition";

    String HEADER_CONTENT_TYPE = "Content-Type";

    String ACCEPT = "Accept";

    String HEADER_CONTENT_LENGTH = "Content-Length";

    String HEADER_HOST = "Host";

    String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    String APPLICATION_JSON = "application/json";

    String MULTIPART_FORM_DATA = "multipart/form-data";

    String QP_SEP_A = "&";
    String QP_SEP_C = ":";
    String QP_SEP_P = "?";
    String QP_SEP_S = "/";
    String QP_SEP_CSS = "://";

    String NAME_VALUE_SEPARATOR = "=";
}
