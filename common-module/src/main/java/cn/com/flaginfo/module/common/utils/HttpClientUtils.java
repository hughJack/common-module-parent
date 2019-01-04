package cn.com.flaginfo.module.common.utils;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * HTTP连接工具
 * @author: Meng.Liu
 * @date: 2018/11/14 上午10:56
 */
public class HttpClientUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpClientUtils.class);
    static final PoolingHttpClientConnectionManager cm;

    private HttpClientUtils() {
    }

    private static CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    public static String doGet(String url) {
        try {
            CloseableHttpClient httpClient = getHttpClient();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            return content;
        } catch (Exception var6) {
            log.error("ERROR, call http get" + var6.getMessage(), var6);
            return null;
        }
    }

    public static String doGet(String url, Map<String, Object> params) {
        try {
            CloseableHttpClient httpClient = getHttpClient();
            StringBuilder urlBuilder = new StringBuilder(url);
            int i = 0;
            if (params != null && !params.isEmpty()) {
                for(Iterator var5 = params.keySet().iterator(); var5.hasNext(); ++i) {
                    String key = (String)var5.next();
                    urlBuilder.append(i == 0 ? "?" : "&");
                    urlBuilder.append(key).append("=").append(params.get(key));
                }
            }

            HttpGet httpGet = new HttpGet(urlBuilder.toString());
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            return content;
        } catch (Exception var9) {
            log.error("ERROR, call http get" + var9.getMessage(), var9);
            return null;
        }
    }

    public static String doPost(String url, Map<String, String> params) throws IOException {
        try {
            CloseableHttpClient httpClient = getHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Accept", "application/json; charset=UTF-8");
            httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(30000).setConnectTimeout(30000).setSocketTimeout(30000).build();
            StringEntity strEntity = new StringEntity(JSONObject.toJSONString(params), Charset.defaultCharset());
            httpPost.setEntity(strEntity);
            httpPost.setConfig(config);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            return content;
        } catch (Exception var9) {
            log.error("ERROR, call http post" + var9.getMessage(), var9);
            return null;
        }
    }

    public static String doPostWithHeader(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        try {
            CloseableHttpClient httpClient = getHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Accept", "application/json; charset=UTF-8");
            httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
            Iterator heads = headers.entrySet().iterator();

            while(heads.hasNext()) {
                Entry<String, String> entry = (Entry)heads.next();
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(30000).setConnectTimeout(30000).setSocketTimeout(30000).build();
            StringEntity strEntity = new StringEntity(JSONObject.toJSONString(params), Charset.defaultCharset());
            httpPost.setEntity(strEntity);
            httpPost.setConfig(config);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            return content;
        } catch (Exception var10) {
            log.error("ERROR, call http post" + var10.getMessage(), var10);
            return null;
        }
    }

    static {
        LayeredConnectionSocketFactory sslsf = null;
        try {
            SSLContextBuilder builder = SSLContextBuilder.create();
            builder.loadTrustMaterial(null, (x509Certificates, s)->true);
            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, (String[])null, NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            log.error("连接池初始化失败",e);
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        cm =new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
    }
}
