package cn.com.flaginfo.rocketmq.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 上午11:43
 */
@Setter
@Getter
@ToString
@Configuration
@ConfigurationProperties(prefix = "spring.mq.rocket")
public class RocketMqConfig {

    private static String DEFAULT_PRODUCER_GROUP = "Producer_" + UUID.randomUUID().toString().replaceAll("-", "");

    /**
     * 服务器地址
     */
    private String address;
    /**
     * 生产者组
     */
    private String producerGroup;
    /**
     * 最小线程数
     */
    private Integer minThread = 10;
    /**
     * 最大线程数
     */
    private Integer maxThread = 20;
    /**
     * 心跳间隔
     */
    private Integer heartbeatBrokerInterval = 60000;
    /**
     * 最大消息大小
     */
    private Integer maxMessageSize = 2 * 1024 * 1024;
    /**
     * 拉取模式下的线程数
     */
    private Integer pullThreadNum = 100;
    /**
     * VIP通道，为服务器端口号-2的端口
     */
    private Boolean vipChannelEnabled = true;

    public String getProducerGroup() {
        if (null == producerGroup) {
            return DEFAULT_PRODUCER_GROUP;
        } else {
            return producerGroup;
        }
    }
}
