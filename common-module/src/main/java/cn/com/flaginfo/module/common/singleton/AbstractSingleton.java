package cn.com.flaginfo.module.common.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/16 16:55
 */
public class AbstractSingleton {

    private static final ConcurrentHashMap<String, AbstractSingleton> registerMap = new ConcurrentHashMap<>();

    public AbstractSingleton() throws SingletonException {
        String className = this.getClass().getName();
        if (registerMap.containsKey(className)) {
            throw new SingletonException("Cannot construct instance for class " + className + ", since an instance already exists!");
        } else {
            synchronized (registerMap) {
                if (registerMap.containsKey(className)) {
                    throw new SingletonException("Cannot construct instance for class " + className + ", since an instance already exists!");
                } else {
                    registerMap.put(className, this);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractSingleton> T getInstance(Class<T> clazz) throws InstantiationException, InvocationTargetException, IllegalAccessException {
        String className = clazz.getName();
        if (!registerMap.containsKey(className)) {
            synchronized (registerMap) {
                if (!registerMap.containsKey(className)) {
                    Constructor[] constructors = clazz.getDeclaredConstructors();
                    if (null == constructors) {
                        throw new NullPointerException("cannot find the none parameter constructor");
                    }
                    Constructor<T> constructor = constructors[0];
                    constructor.setAccessible(true);
                    return constructor.newInstance();
                }
            }
        }
        return (T) registerMap.get(className);
    }

    public static AbstractSingleton getInstance(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        if (!registerMap.containsKey(className)) {
            Class<? extends AbstractSingleton> clazz = Class.forName(className).asSubclass(AbstractSingleton.class);
            synchronized (registerMap) {
                if (!registerMap.containsKey(className)) {
                    Constructor[] constructors = clazz.getDeclaredConstructors();
                    if (null == constructors) {
                        throw new NullPointerException("cannot find the none parameter constructor");
                    }
                    Constructor<AbstractSingleton> constructor = constructors[0];
                    constructor.setAccessible(true);
                    return constructor.newInstance();
                }
            }
        }
        return registerMap.get(className);
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractSingleton> T getInstance(Class<T> clazz, Class<?>[] parameterTypes, Object[] initArgs)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String className = clazz.getName();
        if (!registerMap.containsKey(className)) {
            synchronized (registerMap) {
                if (!registerMap.containsKey(className)) {
                    Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
                    constructor.setAccessible(true);
                    T instance = constructor.newInstance(initArgs);
                    return instance;
                }
            }
        }
        return (T) registerMap.get(className);
    }


    public static class SingletonException extends Exception {

        private static final long serialVersionUID = -8633183690442262445L;

        private SingletonException(String message) {
            super(message);
        }
    }
}
