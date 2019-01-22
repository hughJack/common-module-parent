package cn.com.flaginfo.module.common.reflect;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: Meng.Liu
 * @date: 2019/1/21 下午3:54
 */
@Getter
@Setter
@Slf4j
public class ClassDescription<T> {

    private Lock lock = new ReentrantLock();

    private transient volatile boolean hasInit = false;

    private Class<T> clazz;

    private Map<String, FieldDescription> fieldsMap = new HashMap<>();

    public ClassDescription(Class<T> clazz) {
        this.clazz = clazz;
    }

    public FieldDescription getField(String name) {
        if (!hasInit) {
            this.lockInitClassFieldDescription(clazz);
        }
        return this.fieldsMap.get(name);
    }

    /**
     * 获取对象的属性值
     *
     * @param fieldName
     * @param object
     * @return
     */
    public Object getFieldValueFromObject(String fieldName, Object object) {
        return this.getField(fieldName).getValueFromObject(object);
    }

    private void lockInitClassFieldDescription(Class clazz) {
        if (null == clazz) {
            throw new DescriptionException("reflect class cannot be null.");
        }
        lock.lock();
        try {
            this.initClassFieldDescription(clazz);
            this.hasInit = true;
        } finally {
            lock.unlock();
        }

    }

    private void initClassFieldDescription(Class clazz) {
        if (null == clazz) {
            throw new DescriptionException("reflect class cannot be null.");
        }
        if (this.hasInit) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        if (null != fields && fields.length > 0) {
            for( Field field : fields ){
                FieldDescription f = new FieldDescription(field, clazz, this);
                this.fieldsMap.put(f.getName(), f);
            }
        }
        if (null != clazz.getSuperclass()) {
            this.initClassFieldDescription(clazz.getSuperclass());
        }
    }

}
