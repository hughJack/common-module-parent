package cn.com.flaginfo.redis.permits;

import cn.com.flaginfo.redis.lock.redisson.IRedissonDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 令牌桶限流器工厂
 * @author: Meng.Liu
 * @date: 2018/11/12 下午4:26
 */
@Component
@Slf4j
@ConditionalOnBean(IRedissonDistributedLock.class)
public class RateLimiterFactory {

    @Autowired
    private IRedissonDistributedLock distributedLock;

    /**
     * 本地持有对象
     */
    private volatile Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    /**
     * @param key              redis key
     * @param permitsPerSecond 每秒产生的令牌数
     * @param maxBurstSeconds  最大存储多少秒的令牌
     * @return
     */
    public RateLimiter build(String key, Double permitsPerSecond, Integer maxBurstSeconds) {
        if (!rateLimiterMap.containsKey(key)) {
            synchronized (this) {
                if (!rateLimiterMap.containsKey(key)) {
                    rateLimiterMap.put(key, new RateLimiter(key, permitsPerSecond, maxBurstSeconds, distributedLock));
                }
            }
        }
        return rateLimiterMap.get(key);
    }


}
