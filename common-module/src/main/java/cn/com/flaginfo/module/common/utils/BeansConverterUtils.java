package cn.com.flaginfo.module.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Meng.Liu
 * @date: 2019/1/23 下午4:08
 */
@Slf4j
public class BeansConverterUtils {


    /**
     * 对象转换
     *
     * @param source
     * @param tClass
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T converter(S source, Class<T> tClass) {
        if (null == source) {
            return null;
        }
        return converterNotNullObject(source, tClass);
    }

    /**
     * 转换非空对象，私有化方法
     * @param source
     * @param tClass
     * @param <S>
     * @param <T>
     * @return
     */
    private static <S, T> T converterNotNullObject(S source, Class<T> tClass){
        T target = newInstance(tClass);
        if (null == target) {
            return null;
        }
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * 对象列表转换，转换结果顺序与源顺序一致
     *
     * @param sources
     * @param tClass
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> List<T> converter(List<S> sources, Class<T> tClass) {
        return converter(sources, tClass, true);
    }

    /**
     * 对象转换，转换结果排序可选
     *
     * @param sources
     * @param tClass
     * @param order
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> List<T> converter(List<S> sources, Class<T> tClass, boolean order) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        }
        return order ?
                sources.stream().filter(Objects::nonNull).map(s -> converter(s, tClass)).collect(Collectors.toList())
                : sources.parallelStream().filter(Objects::nonNull).map(s -> converter(s, tClass)).collect(Collectors.toList());
    }


    /**
     * 对象转换
     * @param sources
     * @param tkClass
     * @param <K>
     * @param <V>
     * @param <TK>
     * @return
     */
    public static <K, V, TK> Map<TK, V> converterMapKey(Map<K, V> sources, Class<TK> tkClass) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyMap();
        }
        Map<TK, V> convertMap = new HashMap<>(sources.size());
        sources.entrySet().parallelStream().forEach(entry ->
                convertMap.put(converter(entry.getKey(), tkClass), entry.getValue() )
        );
        return convertMap;
    }

    /**
     * 对象转换
     *
     * @param sources
     * @param tvClass
     * @param <V>
     * @param <TV>
     * @return
     */
    public static <K, V, TV> Map<K, TV> converterMapValue(Map<K, V> sources, Class<TV> tvClass) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyMap();
        }
        Map<K, TV> convertMap = new HashMap<>(sources.size());
        sources.entrySet().parallelStream().forEach(entry ->
            convertMap.put(entry.getKey(), converter(entry.getValue(), tvClass))
        );
        return convertMap;
    }

    /**
     * 对象转换
     * @param sources
     * @param tkClass
     * @param tvClass
     * @param <K>
     * @param <S>
     * @param <TK>
     * @param <TV>
     * @return
     */
    public static <K, S, TK, TV> Map<TK, TV> converterMapKeyAndValue(Map<K, S> sources, Class<TK> tkClass, Class<TV> tvClass) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyMap();
        }
        Map<TK, TV> convertMap = new HashMap<>(sources.size());
        sources.entrySet().parallelStream().forEach(entry ->
                convertMap.put(converter(entry.getKey(), tkClass), converter(entry.getValue(), tvClass))
        );
        return convertMap;
    }


    /**
     * 初始化泛型对象
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T newInstance(Class<T> tClass) {
        T target = null;
        try {
            target = tClass.newInstance();
        } catch (InstantiationException e) {
            log.error("", e);
        } catch (IllegalAccessException e) {
            log.error("", e);
        }
        return target;
    }
}
