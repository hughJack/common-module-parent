package cn.com.flaginfo.redis.cache;

/**
 * 缓存key格式化
 * @author: Meng.Liu
 * @date: 2019-05-05 15:12
 */
public interface IRedisCacheKeyFormatter {

    /**
     * 格式化方法
     * @param cache 缓存注解对象
     * @param names 方法参数名称
     * @param args 方法参数
     * @param returnType 返回参数
     * @return
     */
    String formatter(RedisCache cache, String[] names, Object[] args, Class<?> returnType);

}
