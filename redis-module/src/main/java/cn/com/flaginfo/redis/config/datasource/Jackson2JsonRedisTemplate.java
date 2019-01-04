package cn.com.flaginfo.redis.config.datasource;

import cn.com.flaginfo.redis.config.selector.RedisDatabaseSelector;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/16 11:30
 */
public class Jackson2JsonRedisTemplate extends RedisTemplate<String, Object> {

    private final int defaultDatabase;

    public Jackson2JsonRedisTemplate(){
        this.setSerializer();
        this.defaultDatabase = 0;
    }

    public Jackson2JsonRedisTemplate(int defaultDatabase){
        this.setSerializer();
        this.defaultDatabase = defaultDatabase;
    }

    public Jackson2JsonRedisTemplate(JedisConnectionFactory jedisConnectionFactory){
        this(0, jedisConnectionFactory);
    }

    public Jackson2JsonRedisTemplate(int defaultDatabase, JedisConnectionFactory jedisConnectionFactory){
        this.defaultDatabase = defaultDatabase;
        this.setSerializer();
        this.setConnectionFactory(jedisConnectionFactory);
        this.afterPropertiesSet();
    }

    private void setSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = this.jackson2JsonRedisSerializer();
        StringRedisSerializer stringRedisSerializer = this.stringRedisSerializer();
        this.setKeySerializer(stringRedisSerializer);
        this.setValueSerializer(jackson2JsonRedisSerializer);
        this.setHashKeySerializer(stringRedisSerializer);
        this.setHashValueSerializer(jackson2JsonRedisSerializer);
    }

    private Jackson2JsonRedisSerializer jackson2JsonRedisSerializer(){
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    private StringRedisSerializer stringRedisSerializer(){
        return new StringRedisSerializer();
    }

    @Override
    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        Integer dbIndex = RedisDatabaseSelector.getInstance(false).getAndClearSelect();
        //如果设置了dbIndex
        if (dbIndex != null) {
            if (connection instanceof JedisConnection) {
                if (((JedisConnection) connection).getNativeConnection().getDB().intValue() != dbIndex) {
                    connection.select(dbIndex);
                }
            } else {
                connection.select(dbIndex);
            }
        } else {
            connection.select(defaultDatabase);
        }
        return super.preProcessConnection(connection, existingConnection);
    }

}
