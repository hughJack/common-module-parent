package cn.com.flaginfo.module.common.reflect;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author: Meng.Liu
 * @date: 2019/1/21 下午4:56
 */
@Slf4j
public class DescriptionCacheFactory {

    private transient volatile static Map<String, ClassDescription> classDescriptionMap = Maps.newConcurrentMap();

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
        synchronized (tClass) {
            classDescription = classDescriptionMap.get(className);
            if (null != classDescription) {
                return classDescription;
            }
            classDescription = new ClassDescription<>(tClass);
            classDescriptionMap.put(className, classDescription);
            return classDescription;
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
        FieldDescription fieldDescription = getFieldDescription(fieldName, object.getClass());
        if( null == fieldDescription ){
           throw new DescriptionException("cannot get FieldDescription with class " + object.getClass());
        }
        return fieldDescription.getValueFromObject(object);
    }

}
