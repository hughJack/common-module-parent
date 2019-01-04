package cn.com.flaginfo.mongodb.config.selector;

import cn.com.flaginfo.module.common.selector.AbstractSelector;
import cn.com.flaginfo.module.common.singleton.AbstractSingleton;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/12 9:42
 */
@Slf4j
public class MongodbSourceSelector extends AbstractSelector<String> {

    private static final ThreadLocal<String> selectHolder = new ThreadLocal<String>();

    private MongodbSourceSelector() throws SingletonException{
        super();
    }

    public static MongodbSourceSelector getInstance(){
        try {
            return AbstractSingleton.getInstance(MongodbSourceSelector.class);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static MongodbSourceSelector getInstance(boolean canBeNull){
        MongodbSourceSelector mongodbSourceSelector = getInstance();
        if( !canBeNull && null == mongodbSourceSelector ){
            throw new NullPointerException("mongo source selector is null.");
        }
        return mongodbSourceSelector;
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