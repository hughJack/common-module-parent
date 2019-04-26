package cn.com.flaginfo.rocketmq.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 上午11:43
 */
@Setter
@Getter
@ToString
@Configuration
@ConfigurationProperties(prefix = "spring.mq.ons")
public class OnsMqConfig {

    /**
     * 阿里云key
     */
    private String accessKey;
    /**
     * 阿里云密码
     */
    private String secretKey;
    /**
     * 服务器地址
     */
    private String address;
    /**
     * 生产者组名称
     */
    private String producerGroupName;
    /**
     * 消费者线程数
     */
    private Integer consumeThreadNumber = 10;
    /**
     * VIP通道，为服务器端口号-2的端口
     */
    private Boolean vipChannelEnabled = false;
}
