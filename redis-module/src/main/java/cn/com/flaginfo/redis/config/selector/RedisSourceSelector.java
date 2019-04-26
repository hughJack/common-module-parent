package cn.com.flaginfo.redis.config.selector;

import cn.com.flaginfo.module.common.selector.AbstractSelector;
import cn.com.flaginfo.module.common.singleton.AbstractSingleton;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/12 9:42
 */
@Slf4j
public class RedisSourceSelector extends AbstractSelector<String> {

    private final ThreadLocal<String> selectHolder = new ThreadLocal<>();

    private RedisSourceSelector() throws SingletonException{
        super();
    };

    public static RedisSourceSelector getInstance(){
        try {
            return AbstractSingleton.getInstance(RedisSourceSelector.class);
        } catch (Exception e) {
           log.error("", e);
        }
        return null;
    }

    public static RedisSourceSelector getInstance(boolean canBeNull){
        RedisSourceSelector redisSourceSelector = getInstance();
        if( !canBeNull && null == redisSourceSelector ){
            throw new NullPointerException("redis source selector is null");
        }
        return redisSourceSelector;
    }

    @Override
    public String selected() {
        return selectHolder.get();
    }

    @Override
    public void clearSelected() {
        selectHolder.remove();
    }

    @Override
    public void select(String value) {
        selectHolder.set(value);
    }
}