package cn.com.flaginfo.redis.config.datasource;

import cn.com.flaginfo.redis.RedisMultiTemplateRouting;
import cn.com.flaginfo.redis.config.properties.RedisPoolProperties;
import cn.com.flaginfo.redis.config.properties.RedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/15 16:39
 */
@Slf4j
public abstract class AbstractRedisTemplate extends RedisProperties {

    @Autowired
    private RedisMultiTemplateRouting redisMultiTemplateRouting;

    @Autowired
    private RedisPoolProperties redisPoolProperties;

    /**
     * 配置Key的生成方式
     *
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return (Object o, Method method, Object... objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(o.getClass().getName())
                    .append(method.getName());
            for (Object object : objects) {
                stringBuilder.append(object.toString());
            }
            return stringBuilder.toString();
        };
    }

    /**
     * 配置CacheManager
     *
     * @return
     */
    @Bean
    public CacheManager cacheManager() {
        // 设置缓存有效期10分钟
        log.info("init cache manager...");
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10));
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(this.createJedisConnectionFactory(redisPoolProperties)))
                .cacheDefaults(redisCacheConfiguration).build();
    }

    public Jackson2JsonRedisTemplate instanceRedisTemplate() throws Exception {
        log.info(this.getId() + " init jackson redis template...");
        Jackson2JsonRedisTemplate redisTemplate = new Jackson2JsonRedisTemplate(this.getDatabase());
        redisTemplate.setConnectionFactory(this.createJedisConnectionFactory(redisPoolProperties));
        this.registerRedisTemplate(redisTemplate);
        return redisTemplate;
    }

    private JedisConnectionFactory createJedisConnectionFactory(RedisPoolProperties redisPoolProperties){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(this.getDatabase());
        redisStandaloneConfiguration.setHostName(this.getHost());
        redisStandaloneConfiguration.setPort(this.getPort());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(this.getPassword()));
        //才用连接池后不需要配置timeout
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpb =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
        jpb.poolConfig(this.buildPoolConfig(redisPoolProperties));
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jpb.build());
        return jedisConnectionFactory;
    }

    /**
     * 设置连接池属性
     *
     * @param redisPoolProperties
     * @return
     */
    private JedisPoolConfig buildPoolConfig(RedisPoolProperties redisPoolProperties) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisPoolProperties.getMaxIdle());
        poolConfig.setMinIdle(redisPoolProperties.getMinIdle());
        poolConfig.setMaxTotal(redisPoolProperties.getMaxActive());
        poolConfig.setMaxWaitMillis(redisPoolProperties.getMaxWait());
        poolConfig.setTestOnBorrow(redisPoolProperties.isTestOnBorrow());
        return poolConfig;
    }

    public void registerRedisTemplate(Jackson2JsonRedisTemplate redisTemplate) throws IllegalAccessException {
        redisMultiTemplateRouting.registerTemplate(this.getId(), redisTemplate);
        if (this.isDefault()) {
            redisMultiTemplateRouting.registerDefault(redisTemplate);
        }
    }

    /**
     * 针对数据源生成RedisTemplate
     *
     * @return
     * @throws Exception
     */
    abstract public RedisTemplate<String, Object> getRedisTemplate() throws Exception;
}
