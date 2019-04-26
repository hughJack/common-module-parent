package cn.com.flaginfo.module.common.domain.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: Meng.Liu
 * @date: 2019/4/25 上午9:58
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class HttpClientPoolConfig {

    private int connectTimeout;
    private int socketTimeout;
    private int maxPoolSize;
    private int maxPreRout;
    private int maxRoute;
    private int idleTimeout;
    private int monitorInterval;
    private int retryTimes;

    private HttpClientPoolConfig() {
    }

    public static HttpClientPoolConfigBuilder builder() {
        return new HttpClientPoolConfigBuilder();
    }

    public static class HttpClientPoolConfigBuilder {
        /**
         * 连接建立的超时时间, 10000ms
         */
        private int connectTimeout = 10000;
        /**
         * socket超时
         */
        private int socketTimeout = 10000;
        /**
         * 连接池的最大连接数
         */
        private int maxPoolSize = 40;
        /**
         * 每个路由的最大连接数
         */
        private int maxPreRout = 2;
        /**
         * 每个路由的默认最大连接数
         */
        private int maxRoute = 2;
        /**
         * 空闲超时，默认60s
         */
        private int idleTimeout = 60;
        /**
         * 守护线程间隔，默认10s
         */
        private int monitorInterval = 15;
        /**
         * 请求重试次数
         */
        private int retryTimes = 3;

        HttpClientPoolConfigBuilder() {
        }

        public HttpClientPoolConfigBuilder connectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public HttpClientPoolConfigBuilder socketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        public HttpClientPoolConfigBuilder maxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }

        public HttpClientPoolConfigBuilder maxPreRout(int maxPreRout) {
            this.maxPreRout = maxPreRout;
            return this;
        }

        public HttpClientPoolConfigBuilder maxRoute(int maxRoute) {
            this.maxRoute = maxRoute;
            return this;
        }

        public HttpClientPoolConfigBuilder idleTimeout(int idleTimeout) {
            this.idleTimeout = idleTimeout;
            return this;
        }

        public HttpClientPoolConfigBuilder monitorInterval(int monitorInterval) {
            this.monitorInterval = monitorInterval;
            return this;
        }

        public HttpClientPoolConfigBuilder retryTimes(int retryTimes) {
            this.retryTimes = retryTimes;
            return this;
        }

        public HttpClientPoolConfig build() {
            return new HttpClientPoolConfig(this.connectTimeout,
                    this.socketTimeout,
                    this.maxPoolSize,
                    this.maxPreRout,
                    this.maxRoute,
                    this.idleTimeout,
                    this.monitorInterval,
                    this.retryTimes);
        }
    }
}
