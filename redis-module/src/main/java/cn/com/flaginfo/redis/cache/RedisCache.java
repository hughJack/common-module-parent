package cn.com.flaginfo.redis.cache;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: Meng.Liu
 * @date: 2018/11/20 下午4:14
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCache {

    /**
     * 数据源,当不配置或者为空时采用默认数据源
     * @return
     */
    String dataSource() default "";

    /**
     * 数据库编号
     * 当不配置或者值为-1时采用默认库
     * @return
     */
    int database() default -1;

    /**
     * 缓存的key
     * 支持预编译符，例如: Cache:#{0}:#{memberId}:#{authInfo.id}
     * #{0}表示第0个参数，后面的依次类推
     * #{memberId}表示参数名称为memberId的参数
     * #{authInfo.id}表示参数名为authInfo的对象的id属性
     * @return
     */
    String cacheKey();


    /**
     * 是否使用单线程加载
     * true: 是
     * false: 否
     * 当选择是时会创建1个分布式同步锁，加锁加载数据源，防止高并发场景时的业务并发
     * 能够获取锁的会执行业务方法加载业务
     * 不能获取锁的直接进行等待，等待超时后直接返回空
     * @return
     */
    boolean singleLoader() default false;

    /**
     * 获取锁的时间
     * 单位 : 毫秒(ms)
     * @return
     */
    long tryLockTimeout() default 100;

    /**
     * 锁的有效时间
     * 单位 : 秒(s)
     * @return
     */
    int lockExpire() default 60;

    /**
     * 缓存过期时间
     * 默认60秒，与timeUnit结合使用
     * -1则不过期
     */
    int expire() default 60;

    /**
     * 过期时间单位，默认为秒
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 是否忽略空
     * 默认null的数据也做缓存处理
     * false:不忽略
     * true:忽略
     * @return
     */
    boolean ignoreNull() default false;

    /**
     * 操作类型
     * @return
     */
    OperationType opsType() default OperationType.READ;

    /**
     * 是否启用本地缓存，利用内存存储
     * @return
     */
    boolean enableLocalCache() default false;

    /**
     * 本地缓存名称
     * 如果不设置或者设置为空将采用返回值得ClassName作为该名称，
     * 相同名称共享同一个缓存对象
     * @return
     */
    String localCacheName() default "";

    /**
     * 本地缓存最大记录数，该数值会影响JVM内存大小
     * 建议根据实际情况设置
     * @return
     */
    int localCacheSize() default 1000;

    /**
     * 本地缓存的过期时间
     * @return
     */
    int localCacheExpire() default 60;

    /**
     * 本地缓存的过期时间单位，默认秒
     * @return
     */
    TimeUnit localCacheTimeUnit() default TimeUnit.SECONDS;

    /**
     * 操作类型
     */
    public enum OperationType {
        /**
         * 从缓存读取
         * 如果不存在则执行方法并存入缓存，
         * 否则直接返回缓存数据
         * 当启用本地缓存时，会先从本地缓存读取，再从Redis缓存读取，当两者都不存在时才会执行方法
         */
        READ,
        /**
         * 更新缓存
         * 执行方法，将结果更新至缓存中，返回执行的结果
         */
        UPDATE,
        /**
         * 删除缓存
         * 同时删除Redis缓存和本地缓存
         * 执行方法，删除缓存内容
         */
        DELETE;
    }
}
