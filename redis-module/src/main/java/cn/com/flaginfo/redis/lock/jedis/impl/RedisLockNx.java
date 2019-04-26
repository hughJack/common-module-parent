package cn.com.flaginfo.redis.lock.jedis.impl;

import cn.com.flaginfo.redis.lock.jedis.AbstractRedisLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.redisson.api.RLock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁（这种方式服务器时间一定要同步，否则会出问题）
 *
 * 执行步骤
 * 1. setnx(lockkey, 当前时间+过期超时时间) ，如果返回1，则获取锁成功；如果返回0则没有获取到锁，转向2。
 * 2. get(lockkey)获取值oldExpireTime ，并将这个value值与当前的系统时间进行比较，如果小于当前系统时间，则认为这个锁已经超时，可以允许别的请求重新获取，转向3。
 * 3. 计算newExpireTime=当前时间+过期超时时间，然后getset(lockkey, newExpireTime) 会返回当前lockkey的值currentExpireTime。
 * 4. 判断currentExpireTime与oldExpireTime 是否相等，如果相等，说明当前getset设置成功，获取到了锁。如果不相等，说明这个锁又被别的请求获取走了，那么当前请求可以直接返回失败，或者继续重试。
 * 5. 在获取到锁之后，当前线程可以开始自己的业务处理，当处理完毕后，比较自己的处理时间和对于锁设置的超时时间，如果小于锁设置的超时时间，则直接执行delete释放锁；如果大于锁设置的超时时间，则不需要再锁进行处理。
 *
 * @author: Meng.Liu
 * @date: 2018/11/16 下午1:48
 */
@Slf4j
public class RedisLockNx extends AbstractRedisLock {
    /**
     * 锁的有效时间
     */
    private long expires = 0;

    /**
     * 使用默认的锁过期时间和请求锁的超时时间
     *
     * @param lockKey       锁的key（Redis的Key）
     */
    public RedisLockNx( String lockKey) {
        super(lockKey);
    }

    /**
     * 使用默认的请求锁的超时时间，指定锁的过期时间
     *
     * @param lockKey       锁的key（Redis的Key）
     * @param expireTime    锁的过期时间(单位：秒)
     */
    public RedisLockNx( String lockKey, int expireTime) {
        super(lockKey, expireTime);
    }

    /**
     * 使用默认的锁的过期时间，指定请求锁的超时时间
     *
     * @param lockKey       锁的key（Redis的Key）
     * @param timeOut       请求锁的超时时间(单位：毫秒)
     */
    public RedisLockNx( String lockKey, long timeOut) {
        super(lockKey, timeOut);
    }

    /**
     * 锁的过期时间和请求锁的超时时间都是用指定的值
     *
     * @param lockKey       锁的key（Redis的Key）
     * @param expireTime    锁的过期时间(单位：秒)
     * @param timeOut       请求锁的超时时间(单位：毫秒)
     */
    public RedisLockNx( String lockKey, int expireTime, long timeOut) {
        super(lockKey, expireTime, timeOut);
    }

    /**
     * 使用默认的锁过期时间和请求锁的超时时间
     *
     * @param lockKey       锁的key（Redis的Key）
     */
    public RedisLockNx( String lockKey, String selectType, int database) {
        super(lockKey, selectType, database);
    }

    /**
     * 使用默认的请求锁的超时时间，指定锁的过期时间
     *
     * @param lockKey       锁的key（Redis的Key）
     * @param expireTime    锁的过期时间(单位：秒)
     */
    public RedisLockNx( String lockKey, int expireTime, String selectType, int database) {
        super(lockKey, expireTime, selectType, database);
    }

    /**
     * 使用默认的锁的过期时间，指定请求锁的超时时间
     *
     * @param lockKey       锁的key（Redis的Key）
     * @param timeOut       请求锁的超时时间(单位：毫秒)
     */
    public RedisLockNx( String lockKey, long timeOut, String selectType, int database) {
        super(lockKey, timeOut, selectType, database);
    }

    /**
     * 锁的过期时间和请求锁的超时时间都是用指定的值
     *
     * @param lockKey       锁的key（Redis的Key）
     * @param expireTime    锁的过期时间(单位：秒)
     * @param timeOut       请求锁的超时时间(单位：毫秒)
     */
    public RedisLockNx( String lockKey, int expireTime, long timeOut, String selectType, int database) {
        super(lockKey, expireTime, timeOut, selectType, database);
    }

    @Override
    public boolean tryLock() {
        return lock();
    }

    /**
     * 获得 lock.
     * 实现思路: 主要是使用了redis 的setnx命令,缓存了锁.
     * reids缓存的key是锁的key,所有的共享, value是锁的到期时间(注意:这里把过期时间放在value了,没有时间上设置其超时时间)
     * 执行过程:
     * 1.通过setnx尝试设置某个key的值,成功(当前没有这个锁)则返回,成功获得锁
     * 2.锁已经存在则获取锁的到期时间,和当前时间比较,超时的话,则设置新的值
     *
     * @return true if lock is acquired, false acquire timeouted
     * @throws InterruptedException in case of thread interruption
     */
    @Override
    public boolean lock() {
        // 请求锁超时时间，纳秒
        long timeout = timeOut * 1000000;
        // 系统当前时间，纳秒
        long nowTime = System.nanoTime();

        while ((System.nanoTime() - nowTime) < timeout) {
            expires = System.currentTimeMillis() + expireTime * 1000 + 1 * 1000;
            String expiresStr = String.valueOf(expires);
            Boolean bool = operationForValue().setIfAbsent(lockKey, expiresStr);
            if (BooleanUtils.isTrue(bool)) {
                locked = true;
                redisTemplate.expire(lockKey, expireTime, TimeUnit.SECONDS);
                return true;
            }
            String currentValueStr = String.valueOf(operationForValue().get(lockKey));
            if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                String oldValueStr = String.valueOf(operationForValue().getAndSet(lockKey, expiresStr));
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    locked = true;
                    return true;
                }
            }
            try {
                Thread.sleep(10, random.nextInt(50000));
            } catch (InterruptedException e) {
                log.error("获取分布式锁休眠被中断：", e);
            }

        }
        locked = false;
        return locked;
    }

    /**
     * 解锁
     */
    @Override
    public boolean unlock() {
        // 只有加锁成功并且锁还有效才去释放锁
        if (locked && expires > System.currentTimeMillis()) {
            redisTemplate.delete(lockKey);
            locked = false;
        }
        return true;
    }

}