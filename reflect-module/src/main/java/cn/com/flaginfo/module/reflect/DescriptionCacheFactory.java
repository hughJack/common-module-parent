package cn.com.flaginfo.module.reflect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: Meng.Liu
 * @date: 2019/1/21 下午4:56
 */
@Slf4j
public class DescriptionCacheFactory {

    private transient volatile static Map<String, ClassDescription> classDescriptionMap = new ConcurrentHashMap<>();

    private static final Lock LOCK = new ReentrantLock();

    /**
     * 获取类描述
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> ClassDescription<T> getClassDescription(Class<T> tClass) {
        if (null == tClass) {
            throw new DescriptionException("cannot get class description with null");
        }
        String className = tClass.getName();
        ClassDescription<T> classDescription = classDescriptionMap.get(className);
        if (null != classDescription) {
            return classDescription;
        }
        LOCK.lock();
        try {
            classDescription = classDescriptionMap.get(className);
            if (null != classDescription) {
                return classDescription;
            }
            classDescription = new ClassDescription<>(tClass);
            classDescriptionMap.put(className, classDescription);
            return classDescription;
        }finally {
            LOCK.unlock();
        }
    }

    /**
     * 获取类描述
     *
     * @param className
     * @return
     */
    public static ClassDescription getClassDescription(String className) {
        if (StringUtils.isBlank(className)) {
            throw new DescriptionException("cannot get class description with classname " + className);
        }
        ClassDescription classDescription = classDescriptionMap.get(className);
        if (null != classDescription) {
            return classDescription;
        }
        LOCK.lock();
        try {
            Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(className);
            classDescription = classDescriptionMap.get(className);
            if (null != classDescription) {
                return classDescription;
            }
            classDescription = new ClassDescription<>(loadClass);
            classDescriptionMap.put(className, classDescription);
            return classDescription;
        } catch (ClassNotFoundException e) {
            log.error("", e);
            return null;
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 获取类描述
     *
     * @param fieldName
     * @param tClass
     * @return
     */
    public static <T> FieldDescription getFieldDescription(String fieldName, Class<T> tClass) {
        return getClassDescription(tClass).getField(fieldName);
    }

    /**
     * 获取类对象的属性值
     *
     * @param fieldName
     * @return
     */
    public static Object getFieldValueFromObject(String fieldName, Object object) {
        if( log.isDebugEnabled() ){
            log.debug("get field value form object : {}", object.getClass());
        }
        FieldDescription fieldDescription = getFieldDescription(fieldName, object.getClass());
        if( null == fieldDescription ){
           throw new DescriptionException("cannot get FieldDescription for ["+ fieldName +"] with class " + object.getClass());
        }
        if( log.isDebugEnabled() ){
            log.debug("field description : {}", fieldDescription.getClazz());
        }
        return fieldDescription.getValueFromObject(object);
    }

}
