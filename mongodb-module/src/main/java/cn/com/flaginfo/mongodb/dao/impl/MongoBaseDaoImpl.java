package cn.com.flaginfo.mongodb.dao.impl;

import cn.com.flaginfo.module.reflect.ReflectionUtils;
import cn.com.flaginfo.mongodb.config.MongoDBMultiTemplateRouting;
import cn.com.flaginfo.mongodb.dao.IMongoBaseDao;
import cn.com.flaginfo.mongodb.domain.IBaseMongoPO;
import cn.com.flaginfo.mongodb.vo.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/11 10:15
 */
@Slf4j
public class MongoBaseDaoImpl<T extends IBaseMongoPO> implements IMongoBaseDao<T> {

    /**
     * spring mongodb　集成操作类
     */
    @Autowired
    private MongoDBMultiTemplateRouting mongoDBMultiTemplate;

    @Override
    public List<T> find(Query query) {
        return this.getMongoTemplate().find(query, this.getEntityClass());
    }

    @Override
    public T findOne(Query query) {
        return this.getMongoTemplate().findOne(query, this.getEntityClass());
    }

    @Override
    public void update(Query query, Update update) {
        this.getMongoTemplate().findAndModify(query, update, this.getEntityClass());
    }

    @Override
    public T save(T entity) {
        this.getMongoTemplate().save(entity);
        return entity;
    }

    @Override
    public T insert(T entity) {
        this.getMongoTemplate().insert(entity);
        return entity;
    }

    @Override
    public void insertMany(List<T> entityList){
        if(CollectionUtils.isEmpty(entityList)){
            return;
        }
        this.getMongoTemplate().insertAll(entityList);
    }

    @Override
    public T findById(String id) {
        return this.getMongoTemplate().findById(id, this.getEntityClass());
    }

    @Override
    public T findById(String id, String collectionName) {
        return this.getMongoTemplate().findById(id, this.getEntityClass(), collectionName);
    }

    @Override
    public Page<T> findPage(Page<T> page, Query query) {
        int pageSize = page.getPageSize();
        query.skip(page.offset()).limit(pageSize);
        List<T> rows = this.find(query);
        long total = this.count(query);
        page.setTotal(total);
        page.setData(rows);
        return page;
    }

    @Override
    public long count(Query query) {
        return this.getMongoTemplate().count(query, this.getEntityClass());
    }

    @Override
    public void delete(String id){
        T entity = this.findById(id);
        if( null != entity ){
            this.delete(entity);
        }
    }

    @Override
    public void delete(Query query){
        this.getMongoTemplate().remove(query, this.getEntityClass());
    }

    @Override
    public void delete(T entity){
        this.getMongoTemplate().remove(entity);
    }

    /**
     * 获取需要操作的实体类class
     * @return
     */
    private Class<T> getEntityClass() {
        return ReflectionUtils.getSuperClassGenricType(getClass());
    }


    @Override
    public MongoTemplate getMongoTemplate(){
        if( null == mongoDBMultiTemplate){
            throw new NullPointerException("the mongo multiple template has not been initialized");
        }
        return mongoDBMultiTemplate.getTemplate();
    }
}
