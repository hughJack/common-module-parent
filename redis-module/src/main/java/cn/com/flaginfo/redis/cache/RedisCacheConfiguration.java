package cn.com.flaginfo.redis.cache;

import cn.com.flaginfo.module.common.diamond.DiamondProperties;
import cn.com.flaginfo.module.common.utils.ClassScannerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: Meng.Liu
 * @date: 2018/11/20 下午5:38
 */
@Slf4j
@Configuration
public class RedisCacheConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    private Set<RedisCache> readMap = new HashSet<>();
    private Set<RedisCache> updateMap = new HashSet<>();
    private Set<RedisCache> deleteMap = new HashSet<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (null != contextRefreshedEvent.getApplicationContext().getParent()) {
            return;
        }
        // 扫描的包名
        final String basePackage = DiamondProperties.getPropertyString("redis.cache.scan.base-package");
        try {

            List<Class<?>> classList = ClassScannerUtils.scanner(basePackage);
            if (CollectionUtils.isEmpty(classList)) {
                return;
            }
            for (Class<?> clazz : classList) {
                Method[] methods = clazz.getMethods();
                if (null == methods || methods.length == 0) {
                    continue;
                }
                for (Method method : methods) {
                    RedisCache redisCache = AnnotationUtils.findAnnotation(method, RedisCache.class);
                    if (null != redisCache) {
                        switch (redisCache.opsType()) {
                            case READ:
                                readMap.add(redisCache);
                                break;
                            case DELETE:
                                deleteMap.add(redisCache);
                                break;
                            case UPDATE:
                                updateMap.add(redisCache);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            this.initReadLocalCache();
            this.initUpdateLocalCache();
            this.initDeleteLocalCache();
        } catch (IOException | ClassNotFoundException e) {
            log.error("", e);
        }
    }

    private void initReadLocalCache(){
        log.info("init read local cache...");
        this.initLocalCache(readMap);
        readMap = null;
    }

    private void initDeleteLocalCache(){
        log.info("init delete local cache...");
        this.initLocalCache(deleteMap);
        deleteMap.clear();
        deleteMap = null;
    }

    private void initUpdateLocalCache(){
        log.info("init update local cache...");
        this.initLocalCache(updateMap);
        updateMap = null;
    }

    private void initLocalCache(Set<RedisCache> redisCacheSet) {
        if (CollectionUtils.isEmpty(redisCacheSet)) {
            return;
        }
        redisCacheSet.stream()
                .forEach(redisCache -> RedisLocalCacheFactory.createIfNotExist(redisCache));
        redisCacheSet.clear();
    }

}
