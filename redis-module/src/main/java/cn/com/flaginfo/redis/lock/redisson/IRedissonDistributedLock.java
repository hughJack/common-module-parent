package cn.com.flaginfo.redis.lock.redisson;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * 基于RedLock实现的分布式锁
 * @author: Meng.Liu
 * @date: 2018/11/16 下午1:48
 */
public interface IRedissonDistributedLock {

    /**
     * 获取锁
     * @param lockKey
     * @return
     */
    RLock lock(String lockKey);

    /**
     * 获取锁，设置锁超时时长
     * @param lockKey
     * @param leaseTime
     * @return
     */
    RLock lock(String lockKey, long leaseTime);

    /**
     * 获取锁，设置锁超时时长
     * @param lockKey
     * @param leaseTime
     * @param timeUnit
     * @return
     */
    RLock lock(String lockKey, long leaseTime, TimeUnit timeUnit);

    /**
     * 解锁
     * @param rLock
     */
    void unLock(RLock rLock);

    /**
     * 尝试获取锁
     * @param lockKey
     * @param waitTime
     * @param leaseTime
     * @param timeUnit
     * @return
     */
    boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit timeUnit);

    /**
     * 释放锁
     * @param lockKey
     */
    void unLock(String lockKey);
}
