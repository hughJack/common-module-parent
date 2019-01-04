package cn.com.flaginfo.redis.cache;

import cn.com.flaginfo.module.common.domain.LocalCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Meng.Liu
 * @date: 2018/11/21 上午10:06
 */
@Slf4j
public class RedisLocalCacheFactory {

    private transient volatile static ConcurrentHashMap<String, LocalCache> caches = new ConcurrentHashMap<>();

    /**
     * 创建本地缓存对象，如果对象不存在
     * @param redisCache
     */
    public synchronized static void createIfNotExist(RedisCache redisCache) {
        if( null == redisCache){
            return;
        }
        if (!redisCache.enableLocalCache()) {
            return;
        }
        String cacheName = getCacheName(redisCache);
        if( caches.containsKey(cacheName) ){
            return;
        }
        LocalCache localCache = new LocalCache(cacheName, redisCache.localCacheSize(), redisCache.localCacheExpire(), redisCache.localCacheTimeUnit());
        caches.put(cacheName, localCache);
        if( log.isDebugEnabled() ){
            log.debug("Create Local-Cache : named:{}, maxSize:{}, expire:{}, timeUnit:{}", cacheName, redisCache.localCacheSize(), redisCache.localCacheExpire(), redisCache.localCacheTimeUnit());
        }
    }

    /**
     * 创建本地缓存对象，如果对象不存在
     * @param redisCache
     */
    @Nullable
    public static LocalCache getLocalCache(RedisCache redisCache) {
        if( null == redisCache){
            return null;
        }
        if (!redisCache.enableLocalCache()) {
            return null;
        }
        String cacheName = getCacheName(redisCache);
        if( caches.containsKey(cacheName) ){
            return caches.get(cacheName);
        }else{
            createIfNotExist(redisCache);
            return caches.get(cacheName);
        }
    }

    private static String getCacheName(RedisCache redisCache){
        String cacheName = redisCache.localCacheName();
        if (StringUtils.isBlank(cacheName)) {
            cacheName = redisCache.cacheKey();
        }
        return cacheName;
    }
}
