package cn.com.flaginfo.rocketmq.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 上午11:50
 */
@Setter
@Getter
@ToString
@Configuration
@ConfigurationProperties(prefix = "spring.mq")
public class MqConfig {
    /**
     * MQ类型
     */
    private MqType type;
}
