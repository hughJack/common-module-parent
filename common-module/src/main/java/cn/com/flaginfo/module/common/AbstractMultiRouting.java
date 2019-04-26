package cn.com.flaginfo.module.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/15 16:44
 */
@Slf4j
public abstract class AbstractMultiRouting<T> {

    private final ConcurrentHashMap<String, T> templateMap = new ConcurrentHashMap<>();
    private T defaultTemplate;

    /**
     * 注册MongoDB数据源
     * @param id
     * @param template
     * @throws IllegalAccessException
     */
    public void registerTemplate(String id, T template) throws IllegalAccessException{
        if( StringUtils.isBlank(id) ){
            throw new NullPointerException("register distributed is null");
        }
        if( null == template ){
            throw new NullPointerException("register template is null");
        }
        T oldTemplate = templateMap.get(id);
        if( oldTemplate != null ){
            throw new IllegalAccessException("the template already exists, if you want to cover it, please pass in the cover parameter.");
        }
        templateMap.put(id, template);
    }

    /**
     * 注册MongoDB数据源
     * @param id
     * @param template
     * @param isCover 如果存在是否覆盖
     * @return
     */
    public boolean registerTemplate(String id, T template, boolean isCover){
        try {
            this.registerTemplate(id, template);
        }catch (IllegalAccessException e){
            if( isCover ){
                templateMap.put(id, template);
                return true;
            }
        }
        return false;
    }

    /**
     * 注册默认数据源
     * @param template
     * @throws IllegalAccessException
     */
    public void registerDefault(T template) throws IllegalAccessException{
        if( null != this.defaultTemplate ){
            throw new IllegalAccessException("the default template already exists, if you want to cover it, please pass in the cover parameter.");
        }
        this.defaultTemplate = template;
    }

    /**
     * 注册默认数据源
     * @param template
     * @param isCover 如果存在是否覆盖
     * @throws IllegalAccessException
     */
    public void registerDefault(T template, boolean isCover){
        if( isCover ){
            this.defaultTemplate = template;
        }else{
            if( null == this.defaultTemplate ){
                this.defaultTemplate = template;
            }
        }
    }

    /**
     * 根据上下文注解自动选中数据源
     * @return
     */
    public T getTemplate(){
        return this.getTemplate(this.getMultiSourceType());
    }

    /**
     * 指定数据源类型获取数据源
     * @param lookupKey 数据源类型
     * @return
     */
    public T getTemplate(String lookupKey){
        return this.getTemplate(lookupKey, true);
    }

    /**
     * 指定数据源类型获取数据源
     * @param lookupKey 数据源类型
     * @param defaultIfNull 如果指定类型不存在是否获取默认数据源
     * @return
     */
    public T getTemplate(String lookupKey, boolean defaultIfNull){
        return this.determineTemplate(lookupKey, defaultIfNull);
    }

    /**
     * 获取数据源
     * @param lookupKey
     * @param defaultIfNull
     * @return
     */
    private T determineTemplate(String lookupKey, boolean defaultIfNull) {
        if(StringUtils.isBlank(lookupKey) && defaultIfNull){
           return defaultTemplate;
        }
        T mongoTemplate = templateMap.get(lookupKey);
        if ( null == mongoTemplate && defaultIfNull ) {
            if( null == defaultTemplate ){
                throw new NullPointerException("the default template is null.");
            }
            mongoTemplate = defaultTemplate;
        }
        if ( null == mongoTemplate ) {
            throw new IllegalStateException("Cannot determine target MongoTemplate for lookup key [" + lookupKey + "]");
        }
        return mongoTemplate;
    }


    /**
     * 获取数据源类型
     * @return
     */
    public abstract String getMultiSourceType();
}
