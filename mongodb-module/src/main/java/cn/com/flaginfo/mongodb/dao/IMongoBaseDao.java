package cn.com.flaginfo.mongodb.dao;

import cn.com.flaginfo.mongodb.domain.IBaseMongoPO;
import cn.com.flaginfo.mongodb.vo.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/11 10:16
 */
 public interface IMongoBaseDao<T extends IBaseMongoPO> {
    /**
     * 通过条件查询实体(集合)
     * @param query
     */
     List<T> find(Query query);

    /**
     * 通过一定的条件查询一个实体
     * @param query
     * @return
     */
     T findOne(Query query);

    /**
     * 通过条件查询更新数据
     * @param query	 * @param update
     * @return
     */
     void update(Query query, Update update);

    /**
     * 保存一个对象到mongodb
     * @param entity
     * @return
     */
     T save(T entity);


    /**
     * 新增一个对象到mongodb
     */
    T insert(T entity);

    /**
     * 通过ID获取记录
     * @param id
     * @return
     */
     T findById(String id);

    /**
     * 通过ID获取记录,并且指定了集合名(表的意思)
     * @param id
     * @param collectionName 集合名
     * @return
     */
     T findById(String id, String collectionName);

    /**
     * 分页查询
     * @param page
     * @param query
     * @return
     */
     Page<T> findPage(Page<T> page, Query query);

    /**
     * 求数据总和
     * @param query
     * @return
     */
     long count(Query query);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    void delete(String id);

    /**
     * 根据条件删除
     * @param query
     * @return
     */
    void delete(Query query);

    /**
     * 根据对象删除
     * @param entity
     * @return
     */
    void delete(T entity);

    /**
     * 获取mongoTemplate
     * @return
     */
     MongoTemplate getMongoTemplate();
}
