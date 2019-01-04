package cn.com.flaginfo.module.common.utils;

import cn.com.flaginfo.module.common.domain.LocalCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/26 11:24
 */
@Component
@Slf4j
public class LocalCacheUtils {

    private static LocalCacheUtils localCacheUtils;

    @Value("${local.cache.maximum.size:1000}")
    private long maximumSize;

    @Value("${local.cache.expire.time:10}")
    private long expireTime;

    private transient volatile static ConcurrentHashMap<String, LocalCache> caches = new ConcurrentHashMap<>();

    private LocalCacheUtils(){};

    @PostConstruct
    private void init(){
        localCacheUtils = this;
    }

    public static LocalCache getLocalCache(String id){
        LocalCache localCache;
        if( caches.containsKey(id) ){
            localCache = caches.get(id);
        }else {
            localCache = localCacheUtils.initCache(id);
        }
        return localCache;
    }

    private LocalCache initCache(String id){
        synchronized (LocalCacheUtils.class){
            if(caches.containsKey(id)){
                return  caches.get(id);
            }else{
                return new LocalCache(id, maximumSize, expireTime);
            }
        }
    }

    public static void registerCache(String id, LocalCache localCache){
        synchronized (LocalCacheUtils.class){
            if( caches.containsKey(id) ){
                log.warn("the local cache for this distributed [{}] has already exists, will be covered.", id);
            }
            caches.put(id, localCache);
        }
    }

}
