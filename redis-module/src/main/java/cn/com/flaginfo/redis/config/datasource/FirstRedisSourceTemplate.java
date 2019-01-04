package cn.com.flaginfo.redis.config.datasource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/15 16:35
 */
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis.first")
@ConditionalOnProperty("spring.data.redis.first.id")
@Order(1)
public class FirstRedisSourceTemplate extends AbstractRedisTemplate {

    @Primary
    @Override
    public @Bean("firstRedisTemplate")
    RedisTemplate<String, Object> getRedisTemplate() throws Exception {
        return this.instanceRedisTemplate();
    }
}
