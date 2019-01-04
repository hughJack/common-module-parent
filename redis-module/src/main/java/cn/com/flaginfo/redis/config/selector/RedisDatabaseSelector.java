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
public class RedisDatabaseSelector extends AbstractSelector<Integer> {

    private final ThreadLocal<Integer> selectHolder = new ThreadLocal<>();

    private RedisDatabaseSelector() throws SingletonException{
        super();
    }

    public static RedisDatabaseSelector getInstance(){
        try {
            return AbstractSingleton.getInstance(RedisDatabaseSelector.class);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static RedisDatabaseSelector getInstance(boolean canBeNull){
        RedisDatabaseSelector redisDatabaseSelector = getInstance();
        if( !canBeNull && null == redisDatabaseSelector ){
            throw new NullPointerException("redis database selector is null");
        }
        return redisDatabaseSelector;
    }

    @Override
    public Integer selected() {
        return selectHolder.get();
    }

    @Override
    public void clearSelected() {
        selectHolder.remove();
    }

    @Override
    public void select(Integer value) {
        selectHolder.set(value);
    }
}