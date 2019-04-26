package cn.com.flaginfo.redis.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/15 16:34
 */
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis.common.pool")
@Order(1)
@Setter
@Getter
public class RedisPoolProperties {

    /**
     * 最大存活数
     */
    private int maxActive;
    /**
     * 最大等待数
     */
    private int maxWait;
    /**
     * 最大空闲连接数
     */
    private int maxIdle;
    /**
     * 最小空闲连接数
     */
    private int minIdle;
    /**
     * 获取连接时是否等待
     */
    private boolean testOnBorrow;

}
