package cn.com.flaginfo.redis.lock.redisson.config;


import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "redisson")
@ConditionalOnProperty("redisson.password")
@Data
public class RedissonProperties {

    /**
     * 连接超时时长
     */
    private int timeout = 3000;
    /**
     * ip
     */
    private String address;
    /**
     * 密码
     */
    private String password;
    /**
     * 连接库
     */
    private int database = 0;
    /**
     * 连接池大小
     */
    private int connectionPoolSize = 64;
    /**
     * 最小连接数
     */
    private int connectionMinimumIdleSize = 10;
    /**
     * 备用服务器连接数
     */
    private int slaveConnectionPoolSize = 250;
    /**
     * 主服务器连接数
     */
    private int masterConnectionPoolSize = 250;
    /**
     * 哨兵地址
     */
    private String[] sentinelAddresses;
    /**
     * 主服务器名称
     */
    private String masterName;
}
