package cn.com.flaginfo.module.common.storage.oss;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Meng.Liu
 * @date: 2018/12/4 上午10:30
 */
@Configuration
@ConfigurationProperties(prefix = "spring.aliyun.storage")
@ConditionalOnProperty("spring.aliyun.storage.endpoint")
@Setter
@Getter
@ToString
public class OssConfiguration {
    /**
     * 服务器地址
     */
    private String endpoint;
    /**
     * 别名，该别名用于配置nginx域名转发，配置后所有文件的访问域名都将只想该路径
     */
    private String endpointAlias;
    /**
     * 阿里云主账号
     */
    private String accessKeyId;
    /**
     * 阿里云秘钥
     */
    private String accessKeySecret;
    /**
     * bucket
     */
    private String bucketName;
    /**
     * 默认文件夹
     */
    private String rootFolder;
}
