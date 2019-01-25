package cn.com.flaginfo.module.common.reflect;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Meng.Liu
 * @date: 2019/1/21 下午3:58
 */
@Setter
@Getter
@Slf4j
public class FieldDescription {

    private static final Map<String, Class> BASE_TYPE_MAP = new HashMap<>();

    static {
        BASE_TYPE_MAP.put("long", Long.class);
        BASE_TYPE_MAP.put("int", Integer.class);
        BASE_TYPE_MAP.put("double", Double.class);
        BASE_TYPE_MAP.put("float", Float.class);
        BASE_TYPE_MAP.put("char", Character.class);
        BASE_TYPE_MAP.put("short", Short.class);
        BASE_TYPE_MAP.put("byte", Byte.class);
        BASE_TYPE_MAP.put("boolean", Boolean.class);
    }

    public FieldDescription(Field field, Class<?> clazz, ClassDescription<?> classDescription) {
        this.classDescription = classDescription;
        this.setClazz(clazz);
        this.setField(field);
    }

    /**
     * 类描述对象
     */
    private ClassDescription<?> classDescription;

    /**
     * 属性归属类对象
     */
    private Class<?> clazz;
    /**
     * 属性字段对象
     */
    private Field field;
    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段类型
     */
    private Class<?> type;
    /**
     * 是否私有
     */
    private boolean accessible;
    /**
     * 读取方法
     */
    private Method readMethod;
    /**
     * 写入方法
     */
    private Method writeMethod;

    /**
     * 注解字段
     */
    public Map<String, Object> annotation = new HashMap<>();

    private PropertyDescriptor propertyDescriptor;

    private void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    private void setField(Field field) {
        this.field = field;
        this.accessible = field.isAccessible();
        this.field.setAccessible(true);
        this.type = this.getType();
        this.name = this.field.getName();
    }

    public Method getReadMethod() throws IntrospectionException {
        if (null == readMethod) {
            this.readMethod = this.getPropertyDescriptor().getReadMethod();
        }
        return readMethod;
    }

    public Method getWriteMethod() throws IntrospectionException {
        if (null == writeMethod) {
            this.writeMethod = this.getPropertyDescriptor().getWriteMethod();
        }
        return writeMethod;
    }

    public PropertyDescriptor getPropertyDescriptor() throws IntrospectionException {
        if (null == propertyDescriptor) {
            propertyDescriptor = new PropertyDescriptor(field.getName(), clazz);
        }
        return propertyDescriptor;
    }

    public Class<?> getType() {
        if (null == this.type) {
            Class<?> type = this.field.getType();
            this.type = BASE_TYPE_MAP.getOrDefault(type.getName(), type);
        }
        return this.type;
    }

    public Object getValueFromObject(Object object) {
        if (null == object) {
            return null;
        }
        try {
            return this.field.get(object);
        } catch (IllegalAccessException e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 查找字段的注解
     * @param clazz
     * @param <A>
     * @return
     */
    public <A extends Annotation> boolean hasAnnotation(Class<A> clazz){
        return null != this.getAnnotation(clazz);
    }

    /**
     * 获取字段的注解
     * @param clazz
     * @param <A>
     * @return
     */
    public <A extends Annotation> A getAnnotation(Class<A> clazz){
        if( null == field || null == clazz  ){
            return null;
        }
        Object object = this.annotation.get(clazz.getName());
        if( null == object ){
            synchronized (this){
                object = this.annotation.get(clazz.getName());
                if( null == object ){
                    object = this.field.getAnnotation(clazz);
                    if( null == object ){
                        object = false;
                    }
                    this.annotation.put(clazz.getName(), object);
                }
            }
        }
       return object instanceof Boolean ? null : (A)object;
    }
}
