package cn.com.flaginfo.module.common.domain;


import cn.com.flaginfo.module.common.utils.LocalCacheUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/26 11:26
 */
@Slf4j
public class LocalCache {

    private final String id;

    private final LoadingCache<Object, Object> cache;

    private final LocalDateTime startTime;

    private long maxCacheSize;

    /**
     * 默认缓存容量，1000
     */
    public static final long DEFAULT_MAX_CACHE_SIZE = 1000;

    private long expireTime;
    /**
     * 默认过期时间10秒
     */
    public static final long DEFAULT_EXPIRE_TIME = 10;

    /**
     * 默认的缓存加载器
     */
    private static final CacheLoader<Object, Object> DEFAULT_CACHE_LOADER = new CacheLoader<Object, Object>() {
        @Override
        public Object load(Object key) throws Exception {
            return null;
        }
    };

    public LocalCache(String id) {
        this(id, DEFAULT_MAX_CACHE_SIZE, DEFAULT_EXPIRE_TIME, DEFAULT_CACHE_LOADER);
    }

    public LocalCache(String id, CacheLoader<Object, Object> cacheLoader) {
        this(id, DEFAULT_MAX_CACHE_SIZE, DEFAULT_EXPIRE_TIME, cacheLoader);
    }

    public LocalCache(String id, long maxCacheSize, long expireTime) {
        this(id, maxCacheSize, expireTime, TimeUnit.SECONDS, DEFAULT_CACHE_LOADER);
    }

    public LocalCache(String id, long maxCacheSize, long expireTime, TimeUnit timeUnit) {
        this(id, maxCacheSize, expireTime, timeUnit, DEFAULT_CACHE_LOADER);
    }

    public LocalCache(String id, long maxCacheSize, long expireTime, CacheLoader<Object, Object> cacheLoader) {
        this(id, maxCacheSize, expireTime, TimeUnit.SECONDS, cacheLoader);
    }

    public LocalCache(String id, long maxCacheSize, long expireTime, TimeUnit timeUnit, CacheLoader<Object, Object> cacheLoader) {
        this.cache = CacheBuilder
                .newBuilder()
                .maximumSize(maxCacheSize)
                .expireAfterWrite(expireTime, timeUnit)
                .recordStats()
                .build(cacheLoader);
        this.id = id;
        this.maxCacheSize = maxCacheSize;
        this.expireTime = expireTime;
        this.startTime = LocalDateTime.now();
        LocalCacheUtils.registerCache(id, this);
    }


    public void put(Object key, Object value){
        try {
            this.cache.put(key, value);
        }catch (Exception e){
            log.error("cannot set value to local cache with key:{}", key);
        }
    }

    public Object get(Object key){
        try {
            return this.cache.get(key);
        }catch (Exception e){
            log.error("cannot get value from local cache with key:{}", key);
        }
        return null;
    }

    public Object remove(Object key){
        Object cacheObj = null;
        try {
            cacheObj = this.get(key);
            this.cache.invalidate(key);
        }catch (Exception e){
            log.error("cannot remove value from local cache with key:{}", key);
        }
        return cacheObj;
    }

    public void clear(){
        try {
            this.cache.invalidateAll();
        }catch (Exception e){
            log.error("cannot clear local cache:{}", this.id);
        }
    }

    public long size(){
        try {
            return this.cache.size();
        }catch (Exception e){
            log.error("cannot get local cache size:{}", this.id);
        }
        return 0;
    }
}
