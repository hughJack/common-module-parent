package cn.com.flaginfo.kafka.consumer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/18 10:28
 */
@ConfigurationProperties(prefix = "kafka.consumer")
@Setter
@Getter
public class KafkaConsumerProperties {

    private List<String> bootstrapServers;
    private Boolean enableAutoCommit = true;
    private Integer autoCommitIntervalMs;
    private Integer sessionTimeoutMs;
    private String autoOffsetReset;
    private Integer maxPollRecords;
    private String keyDeserializer;
    private String valueDeserializer;

    public void setEnableAutoCommit(Boolean enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
    }
}
