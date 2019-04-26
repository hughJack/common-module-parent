package cn.com.flaginfo.redis;

import cn.com.flaginfo.redis.config.selector.RedisDatabaseSelector;
import cn.com.flaginfo.redis.config.selector.RedisSourceSelector;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/16 13:59
 */
@Component
public class RedisUtils {

    private static RedisUtils redisUtils;

    private RedisUtils() {
    }

    @PostConstruct
    private void init() {
        redisUtils = this;
    }

    @Autowired
    private RedisMultiTemplateRouting redisMultiTemplateRouting;

    private HashOperations<String, String, Object> hashOperations() {
        return getTemplate().opsForHash();
    }

    public ValueOperations<String, Object> valueOperations() {
        return getTemplate().opsForValue();
    }

    public ListOperations<String, Object> listOperations() {
        return getTemplate().opsForList();
    }

    public SetOperations<String, Object> setOperations() {
        return getTemplate().opsForSet();
    }

    public ZSetOperations<String, Object> zSetOperations() {
        return getTemplate().opsForZSet();
    }

    public static RedisUtils selectSource(String selectType) {
        RedisSourceSelector.getInstance(false).select(selectType);
        return redisUtils;
    }

    public static RedisUtils selectDatabase(int database) {
        RedisDatabaseSelector.getInstance(false).select(database);
        return redisUtils;
    }

    public static RedisUtils select(String selectType, int database) {
        RedisSourceSelector.getInstance(false).select(selectType);
        RedisDatabaseSelector.getInstance(false).select(database);
        return redisUtils;
    }

    public static RedisUtils select() {
        RedisSourceSelector.getInstance(false).clearSelected();
        RedisDatabaseSelector.getInstance(false).clearSelected();
        return redisUtils;
    }


    public RedisTemplate<String, Object> getTemplate() {
        if (null == redisUtils) {
            throw new NullPointerException("redis util has not been initialized");
        }
        if (null == redisUtils.redisMultiTemplateRouting) {
            throw new NullPointerException("redis multiple template has not been initialized");
        }
        RedisTemplate redisTemplate = redisUtils.redisMultiTemplateRouting.getTemplate();
        if (null == redisTemplate) {
            throw new NullPointerException("redis template is empty.");
        }
        return redisTemplate;
    }

    /**
     * 判断key是否存在
     *
     * @param key
     */
    public boolean hasKey(String key) {
        return getTemplate().hasKey(key);
    }

    /**
     * 删除key
     *
     * @param key
     */
    public void delete(String key) {
        getTemplate().delete(key);
    }

    /**
     * 判断指定key的hashKey是否存在
     *
     * @param key
     * @param hashKey
     * @return
     */
    public boolean hasKey(String key, String hashKey) {
        return hashOperations().hasKey(key, hashKey);
    }

    /**
     * 设置超时时间
     *
     * @param key
     * @param timeout
     * @param unit
     */
    public void expire(String key, final long timeout, final TimeUnit unit) {
        getTemplate().expire(key, timeout, unit);
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @return
     */
    public long ttl(String key) {
        return getTemplate().getExpire(key);
    }

    /**
     * 获取指定pattern的key
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        return getTemplate().keys(pattern);
    }

    /**
     * 删除多个key
     *
     * @param keys
     */
    public void delete(Set<String> keys) {
        getTemplate().delete(keys);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param expire
     */
    private void setExpire(String key, long expire) {
        setExpire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param expire
     */
    private void setExpire(String key, long expire, TimeUnit unit) {
        if (expire != -1) {
            getTemplate().expire(key, expire, unit);
        }
    }

    //---------------------------------------------------------------------
    // ValueOperations -> Redis String/Value 操作
    //---------------------------------------------------------------------

    /**
     * 设置key-value值,传入时间单位
     */
    public long incValue(String key) {
        return incValue(key, 1);
    }

    /**
     * 设置key-value值,传入时间单位
     */
    public long incValue(String key, long val) {
        return valueOperations().increment(key, val);
    }

    /**
     * 设置key-value值
     */
    public void addValue(String key, Object value, long expire) {
        valueOperations().set(key, value);
        setExpire(key, expire);
    }

    /**
     * 设置key-value值,传入时间单位
     */
    public void addValue(String key, Object value, long expire, TimeUnit timeUnit) {
        valueOperations().set(key, value, expire, timeUnit);
    }

    /**
     * 设置key-value值, 无过期时间
     */
    public void addValue(String key, Object value) {
        valueOperations().set(key, value);
    }

    /**
     * 获取key的值
     */
    public Object getValue(String key) {
        return valueOperations().get(key);
    }

    /**
     * 获取指定类型的值
     *
     * @param key
     * @return
     */
    public <T> T getObjectValue(String key, Class<T> tClass) {
        Object object = this.getValue(key);
        if (tClass.isInstance(object)) {
            return (T) object;
        } else if (tClass == Long.class && object instanceof Integer) {
            return (T) (Long.valueOf(((Integer) object).longValue()));
        } else {
            return null;
        }
    }

    //---------------------------------------------------------------------
    // HashOperations -> Redis Redis Hash 操作
    //---------------------------------------------------------------------

    /**
     * 向redis 中添加内容
     *
     * @param key     保存key
     * @param hashKey hashKey
     * @param data    保存对象 data
     * @param expire  过期时间    -1：表示不过期
     */
    public void addHashValue(String key, String hashKey, Object data, long expire) {
        hashOperations().put(key, hashKey, data);
        setExpire(key, expire);
    }

    /**
     * Hash 添加数据
     *
     * @param key key
     * @param map data
     */
    public void addAllHashValue(String key, Map<String, Object> map, long expire) {
        hashOperations().putAll(key, map);
        setExpire(key, expire);
    }

    /**
     * Hash 添加数据
     *
     * @param key key
     * @param map data
     */
    public void addAllHashValue(String key, Map<String, Object> map, long expire, TimeUnit unit) {
        hashOperations().putAll(key, map);
        setExpire(key, expire, unit);
    }

    /**
     * 删除hash key
     *
     * @param key     key
     * @param hashKey hashKey
     */
    public long deleteHashValue(String key, String hashKey) {
        return hashOperations().delete(key, hashKey);
    }

    /**
     * 获取数据
     */
    public Object getHashValue(String key, String hashKey) {
        return hashOperations().get(key, hashKey);
    }

    /**
     * 获取数据
     */
    public Map<String, Object> getHash(String key) {
        return hashOperations().entries(key);
    }

    /**
     * 批量获取数据
     */
    public List<Object> getHashAllValue(String key) {
        return hashOperations().values(key);
    }

    /**
     * 批量获取指定hashKey的数据
     */
    public List<Object> getHashMultiValue(String key, List<String> hashKeys) {
        return hashOperations().multiGet(key, hashKeys);
    }

    /**
     * 获取hash数量
     */
    public Long getHashCount(String key) {
        return hashOperations().size(key);
    }


    //---------------------------------------------------------------------
    // ZSetOperations -> Redis Sort Set 操作
    //---------------------------------------------------------------------

    /**
     * 设置zset值
     */
    public boolean addZSetValue(String key, Object member, long score) {
        return zSetOperations().add(key, member, score);
    }

    /**
     * 批量设置zset值
     */
    public long addZSetValue(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        return zSetOperations().add(key, tuples);
    }

    /**
     * 设置zset值
     */
    public boolean addZSetValue(String key, Object member, double score) {
        return zSetOperations().add(key, member, score);
    }

    /**
     * 批量设置zset值
     */
    public long addBatchZSetValue(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        return zSetOperations().add(key, tuples);
    }

    /**
     * 自增zset值
     */
    public void incZSetValue(String key, String member, long delta) {
        zSetOperations().incrementScore(key, member, delta);
    }

    /**
     * ø
     * 获取zset成员分数
     */
    public long getZSetScore(String key, String member) {
        Double score = zSetOperations().score(key, member);
        if (score == null) {
            return 0;
        } else {
            return score.longValue();
        }
    }

    /**
     * 获取zset长度
     */
    public long getZSetSize(String key) {
        Long size = zSetOperations().zCard(key);
        if (null == size) {
            return 0;
        } else {
            return size;
        }
    }

    /**
     * 删除指定区间的值
     */
    public long removeZRange(String key, long start, long end) {
        Long removeSize = zSetOperations().removeRange(key, start, end);
        if (null == removeSize) {
            return 0;
        } else {
            return removeSize;
        }
    }

    /**
     * 删除指定区间的值
     */
    public long removeZValues(String key, Object... members) {
        if (null == members || members.length == 0) {
            return 0;
        }
        Long removeSize = zSetOperations().remove(key, members);
        if (null == removeSize) {
            return 0;
        } else {
            return removeSize;
        }
    }

    /**
     * 删除分值的数据
     */
    public long removeRangeByScore(String key, double min, double max) {
        Long removeSize = zSetOperations().removeRangeByScore(key, min, max);
        if (null == removeSize) {
            return 0;
        } else {
            return removeSize;
        }
    }

    /**
     * 获取两个有序集合的并集
     */
    public long zUnionStore(String descKey, String... srcKeys) {
        return zUnionStore(descKey, new int[]{1, 1}, srcKeys);
    }

    /**
     * 获取两个有序集合的并集
     */
    public long zUnionStore(String descKey, int[] weight, String... srcKeys) {
        return zUnionStore(descKey, RedisZSetCommands.Aggregate.SUM, weight, srcKeys);
    }

    /**
     * 获取两个有序集合的并集
     */
    public long zUnionStore(String descKey, RedisZSetCommands.Aggregate aggregate, int[] weight, String... srcKeys) {
        if (StringUtils.isBlank(descKey) || srcKeys.length == 0) {
            return 0;
        }
        byte[] descByt = descKey.getBytes();
        byte[][] srcByts = new byte[srcKeys.length][];
        for (int i = 0; i < srcKeys.length; i++) {
            srcByts[i] = srcKeys[i].getBytes();
        }
        Long count = getTemplate().execute(connection -> connection.zUnionStore(descByt, aggregate, weight, srcByts), true);
        if (null == count) {
            return 0;
        } else {
            return count;
        }
    }

    /**
     * 获取两个有序集合的差集
     */
    public long zDiffSet(String descKey, String srcKey1, String srcKey2) {
        long count = zUnionStore(descKey, RedisZSetCommands.Aggregate.MIN, new int[]{1, 0}, srcKey1, srcKey2);
        if (count <= 0) {
            return 0;
        }
        long remCount = removeRangeByScore(descKey, 0, 0);
        return count - remCount;
    }

    /**
     * 获取有序集 key 中成员
     */
    public Set<ZSetOperations.TypedTuple<Object>> getZSetRank(String key, long start, long end) {
        return zSetOperations().rangeWithScores(key, start, end);
    }

    /**
     * 获取有序集 key 中成员，按分数从高到低排序
     */
    public Set<Object> getZSetReverseRange(String key, long start, long end) {
        return zSetOperations().reverseRange(key, start, end);
    }

    /**
     * 获取有序集 key 中成员，按指定分数区间，按分数从高到低排序
     */
    public Set<Object> getZSetReverseRangeByScore(String key, long start, long end) {
        return zSetOperations().reverseRangeByScore(key, start, end);
    }

    /**
     * 获取有序集 key 中成员，按指定分数区间，按分数从高到低排序
     */
    public Set<ZSetOperations.TypedTuple<Object>> getZSetReverseRangeByScoreWithScore(String key, long start, long end) {
        return zSetOperations().reverseRangeByScoreWithScores(key, start, end);
    }

    /**
     * 获取有序集 key 中成员，按指定分数区间，按分数从高到低排序
     */
    public Set<Object> getZSetReverseRangeByScore(String key, long start, long end, long offset, long count) {
        return zSetOperations().reverseRangeByScore(key, start, end, offset, count);
    }

    /**
     * 获取有序集 key 中成员，按指定分数区间，按分数从高到低排序
     */
    public Set<ZSetOperations.TypedTuple<Object>> getZSetReverseRangeByScoreWithScore(String key, long start, long end, long offset, long count) {
        return zSetOperations().reverseRangeByScoreWithScores(key, start, end, offset, count);
    }

    /**
     * 获取有序集 key 中成员按分数从高到低排序的排名
     */
    public long getZRveRank(String key, String member) {
        Long range = zSetOperations().reverseRank(key, member);
        if (null == range) {
            return 0L;
        } else {
            return range;
        }
    }

    /**
     * 迭代所有元素
     *
     * @param key
     * @return
     */
    public Cursor<ZSetOperations.TypedTuple<Object>> scanZSet(String key) {
        return zSetOperations().scan(key, ScanOptions.NONE);
    }

    //---------------------------------------------------------------------
    // listOperations -> Redis List() 操作
    //---------------------------------------------------------------------

    /**
     * 添加list列表
     */
    public void addListValue(String key, Object list) {
        listOperations().leftPush(key, list);
    }

    public void addListAll(String key, Collection<Object> list) {
        listOperations().leftPushAll(key, list);
    }

    /**
     * 获取指定Key对应的list
     */
    public Object getListValue(String key) {
        return listOperations().leftPop(key);
    }

    /**
     * 获取指定Key对应的list的长度
     */
    public Long getListLength(String key) {
        Long length = listOperations().size(key);
        return null == length ? 0 : length;
    }

    public Object getHead(String key) {
        return listOperations().leftPop(key);
    }

    public Object getTail(String key) {
        return listOperations().rightPop(key);
    }

    public void addHead(String key, Object value) {
        listOperations().leftPush(key, value);
    }

    public void addTail(String key, Object value) {
        listOperations().rightPush(key, value);
    }

    public List<Object> getListAll(String key) {
        return this.getListRange(key, 0, -1);
    }

    public List<Object> getListRange(String key, int start, int end) {
        return listOperations().range(key, start, end);
    }

    public void trimList(String key, int start, int end) {
        listOperations().trim(key, start, end);
    }

    public void fixList(String key, int length) {
        listOperations().trim(key, 0, length);
    }

    public Object getAndRemLeft(String key) {
        return listOperations().leftPop(key);
    }

    public Object getAndRemRight(String key) {
        return listOperations().rightPop(key);
    }


    //---------------------------------------------------------------------
    // setOperations -> Redis Set() 操作
    //---------------------------------------------------------------------

    /**
     * 添加Set集合集合
     */
    public void addSetValue(String key, Object list) {
        setOperations().add(key, list);
    }

    /**
     * 获取指定Key对应的set
     */
    public Object getSetValue(String key) {
        return setOperations().members(key);
    }

    /**
     * 是否包含
     */
    public Long sSize(String key) {
        return setOperations().size(key);
    }

    /**
     * 是否包含
     */
    public Object sIsMember(String key, Object val) {
        return setOperations().isMember(key, val);
    }


    /**
     * 获取并移除指定key的值
     */
    public Object popSetValue(String key) {
        return setOperations().pop(key);
    }

    /**
     * 获取set的所有值
     */
    public Set<Object> sMembers(String key) {
        return setOperations().members(key);
    }

    /**
     * 获取集合 key 中的指定个数个随机成员
     */
    public List<Object> sRandomMembers(String key, long count) {
        return getTemplate().opsForSet().randomMembers(key, count);
    }

    /**
     * 获取集合 key 中的一个随机成员
     */
    public Object sRandomMember(String key) {
        return getTemplate().opsForSet().randomMember(key);
    }

    /**
     * 获取redis的当前时间
     */
    public Long currentTime() {
        return getTemplate().execute((RedisConnection connection) -> connection.time());
    }

    public static String buildKey(Object... objects) {
        if (objects.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Object o : objects) {
            builder.append(o.toString()).append(":");
        }
        builder.delete(builder.length() - 1, builder.length());
        return builder.toString();
    }
}
