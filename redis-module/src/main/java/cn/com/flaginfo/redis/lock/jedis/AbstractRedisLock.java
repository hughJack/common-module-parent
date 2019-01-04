package cn.com.flaginfo.redis.lock.jedis;

import cn.com.flaginfo.redis.RedisUtils;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Random;

/**
 * 基于Jedis的NX实现的分布式锁
 * @author: Meng.Liu
 * @date: 2018/11/16 下午1:48
 */
@Data
public abstract class AbstractRedisLock {
    protected final Random random = new Random();

    /**
     * 默认请求锁的超时时间(ms 毫秒)
     */
    private static final long TIME_OUT = 100;

    /**
     * 默认锁的有效时间(s)
     */
    public static final int EXPIRE = 60;

    /**
     * 锁标志对应的key
     */
    protected String lockKey;

    /**
     * 记录到日志的锁标志对应的key
     */
    protected String lockKeyLog = "";

    /**
     * 锁对应的值
     */
    protected String lockValue;

    /**
     * 锁的有效时间(s)
     */
    protected int expireTime = EXPIRE;

    /**
     * 请求锁的超时时间(ms)
     */
    protected long timeOut = TIME_OUT;

    /**
     * 锁标记
     */
    protected volatile boolean locked = false;

    /**
     * redisTemplate
     */
    protected RedisTemplate<String, Object> redisTemplate;

    /**
     * 使用默认的锁过期时间和请求锁的超时时间
     *
     * @param lockKey       锁的key（Redis的Key）
     */
    public AbstractRedisLock(String lockKey) {
        this.redisTemplate = RedisUtils.select().getTemplate();
        this.lockKey = lockKey + "_lock";
    }
    /**
     * 使用默认的请求锁的超时时间，指定锁的过期时间
     *
     * @param lockKey       锁的key（Redis的Key）
     * @param expireTime    锁的过期时间(单位：秒)
     */
    public AbstractRedisLock(String lockKey, int expireTime) {
        this(lockKey);
        this.expireTime = expireTime;
    }

    /**
     * 使用默认的锁的过期时间，指定请求锁的超时时间
     *
     * @param lockKey       锁的key（Redis的Key）
     * @param timeOut       请求锁的超时时间(单位：毫秒)
     */
    public AbstractRedisLock(String lockKey, long timeOut) {
        this( lockKey);
        this.timeOut = timeOut;
    }

    /**
     * 锁的过期时间和请求锁的超时时间都是用指定的值
     *
     * @param lockKey       锁的key（Redis的Key）
     * @param expireTime    锁的过期时间(单位：秒)
     * @param timeOut       请求锁的超时时间(单位：毫秒)
     */
    public AbstractRedisLock(String lockKey, int expireTime, long timeOut) {
        this( lockKey, expireTime);
        this.timeOut = timeOut;
    }

    /**
     * 使用默认的锁过期时间和请求锁的超时时间
     *
     * @param lockKey       锁的key（Redis的Key）
     */
    public AbstractRedisLock(String lockKey, String selectType, int database) {
        this.redisTemplate = RedisUtils.select(selectType, database).getTemplate();
        this.lockKey = lockKey + "_lock";
    }
    /**
     * 使用默认的请求锁的超时时间，指定锁的过期时间
     *
     * @param lockKey       锁的key（Redis的Key）
     * @param expireTime    锁的过期时间(单位：秒)
     */
    public AbstractRedisLock(String lockKey, int expireTime, String selectType, int database) {
        this(lockKey, selectType, database);
        this.expireTime = expireTime;
    }

    /**
     * 使用默认的锁的过期时间，指定请求锁的超时时间
     *
     * @param lockKey       锁的key（Redis的Key）
     * @param timeOut       请求锁的超时时间(单位：毫秒)
     */
    public AbstractRedisLock(String lockKey, long timeOut, String selectType, int database) {
        this( lockKey, selectType, database);
        this.timeOut = timeOut;
    }

    /**
     * 锁的过期时间和请求锁的超时时间都是用指定的值
     *
     * @param lockKey       锁的key（Redis的Key）
     * @param expireTime    锁的过期时间(单位：秒)
     * @param timeOut       请求锁的超时时间(单位：毫秒)
     */
    public AbstractRedisLock(String lockKey, int expireTime, long timeOut, String selectType, int database) {
        this( lockKey, expireTime, selectType, database);
        this.timeOut = timeOut;
    }

    /**
     * 获取操作对象
     * @return
     */
    protected ValueOperations<String, Object> operationForValue(){
        return redisTemplate.opsForValue();
    }



    public abstract boolean tryLock();

    public abstract boolean lock();

    public abstract boolean unlock();
}
