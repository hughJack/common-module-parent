package cn.com.flaginfo.redis.lock.redisson.impl;

import cn.com.flaginfo.redis.lock.redisson.IRedissonDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author liumeng
 */
@Slf4j
public class RedissonDistributedLocker implements IRedissonDistributedLock {

    private RedissonClient redissonClient;

    public RedissonDistributedLocker(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
    }

    @Override
    public RLock lock(String lockKey) {
        RLock rLock = this.getRLock(lockKey);
        rLock.lock();
        return rLock;
    }

    @Override
    public RLock lock(String lockKey, long leaseTime) {
        return this.lock(lockKey, leaseTime, TimeUnit.SECONDS);
    }

    @Override
    public RLock lock(String lockKey, long leaseTime, TimeUnit timeUnit) {
        RLock rLock = this.getRLock(lockKey);
        rLock.lock(leaseTime, timeUnit);
        return rLock;
    }

    @Override
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit timeUnit) {
        RLock rLock = this.getRLock(lockKey);
        try {
            return rLock.tryLock(waitTime, leaseTime, timeUnit);
        } catch (InterruptedException e) {
            log.error("", e);
        }
        return false;
    }

    @Override
    public void unLock(String lockKey) {
        RLock rLock = this.getRLock(lockKey);
        rLock.unlock();
    }

    @Override
    public void unLock(RLock rLock) {
        if( null == rLock ){
            throw new NullPointerException("rLock cannot be null.");
        }
        rLock.unlock();
    }

    private RLock getRLock(String lockKey) {
        if( null == redissonClient ){
            throw new NullPointerException("redisson client is null.");
        }
        return redissonClient.getLock(lockKey);
    }


}
