package cn.com.flaginfo.redis.cache;

import cn.com.flaginfo.module.reflect.AnnotationResolver;

import java.util.regex.Pattern;

/**
 * @author: Meng.Liu
 * @date: 2019-05-05 15:15
 */
public class RedisCacheKeyDefaultFormatter implements IRedisCacheKeyFormatter {


    private static final String PATTERN = ".*\\{+.*\\}+.*";

    @Override
    public String formatter(RedisCache cache, String[] names, Object[] args, Class<?> returnType) {
        if (!Pattern.matches(PATTERN, cache.cacheKey())) {
            return cache.cacheKey();
        }
        return AnnotationResolver.resolver(cache.cacheKey(), names, args, cache.replaceNull());
    }
}
