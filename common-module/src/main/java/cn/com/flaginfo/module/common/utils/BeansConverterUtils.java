package cn.com.flaginfo.module.common.utils;

import cn.com.flaginfo.module.common.annotation.CvtField;
import cn.com.flaginfo.module.common.annotation.CvtTransformHandler;
import cn.com.flaginfo.module.common.reflect.ClassDescription;
import cn.com.flaginfo.module.common.reflect.DescriptionCacheFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        return converter(source, tClass, false);
    }

    /**
     * 对象转换
     *
     * @param source
     * @param tClass
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T converter(S source, Class<T> tClass, boolean withCvt) {
        if (null == source) {
            return null;
        }
        return converterNotNullObject(source, tClass, withCvt);
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
        return converter(sources, tClass, false);
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
    public static <S, T> List<T> converter(List<S> sources, Class<T> tClass, boolean withCvt) {
        return converter(sources, tClass, withCvt, true);
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
    public static <S, T> List<T> converter(List<S> sources, Class<T> tClass, boolean withCvt, boolean order) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        }
        return order ?
                sources.stream().filter(Objects::nonNull).map(s -> converterNotNullObject(s, tClass, withCvt)).collect(Collectors.toList())
                : sources.parallelStream().filter(Objects::nonNull).map(s -> converterNotNullObject(s, tClass, withCvt)).collect(Collectors.toList());
    }

    /**
     * 对象转换
     *
     * @param sources
     * @param tkClass
     * @param <K>
     * @param <V>
     * @param <TK>
     * @return
     */
    public static <K, V, TK> Map<TK, V> converterMapKey(Map<K, V> sources, Class<TK> tkClass) {
        return converterMapKey(sources, tkClass, false);
    }

    /**
     * 对象转换
     *
     * @param sources
     * @param tkClass
     * @param <K>
     * @param <V>
     * @param <TK>
     * @return
     */
    public static <K, V, TK> Map<TK, V> converterMapKey(Map<K, V> sources, Class<TK> tkClass, boolean withCvt) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyMap();
        }
        Map<TK, V> convertMap = new HashMap<>(sources.size());
        sources.entrySet().parallelStream().forEach(entry ->
                convertMap.put(converter(entry.getKey(), tkClass, withCvt), entry.getValue())
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
        return converterMapValue(sources, tvClass, false);
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
    public static <K, V, TV> Map<K, TV> converterMapValue(Map<K, V> sources, Class<TV> tvClass, boolean withCvt) {
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
     *
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
        return converterMapKeyAndValue(sources, tkClass, tvClass, false);
    }

    /**
     * 对象转换
     *
     * @param sources
     * @param tkClass
     * @param tvClass
     * @param <K>
     * @param <S>
     * @param <TK>
     * @param <TV>
     * @return
     */
    public static <K, S, TK, TV> Map<TK, TV> converterMapKeyAndValue(Map<K, S> sources, Class<TK> tkClass, Class<TV> tvClass, boolean withCvt) {
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
     *
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

    /**
     * 转换非空对象，私有化方法
     *
     * @param source
     * @param tClass
     * @param <S>
     * @param <T>
     * @return
     */
    private static <S, T> T converterNotNullObject(S source, Class<T> tClass, boolean withCvt) {
        T target = newInstance(tClass);
        if (null == target) {
            return null;
        }
        BeanUtils.copyProperties(source, target);
        if (withCvt) {
            converterCustomField(source, target);
            return target;
        } else {
            return target;
        }
    }

    /**
     * 转换自定义字段
     */
    public static <S, T> void converterCustomField(S source, T tClass) {
        if (null == source || null == tClass) {
            return;
        }
        ClassDescription<T> classDescription = DescriptionCacheFactory.getClassDescription((Class<T>) tClass.getClass());
        if (null == classDescription || CollectionUtils.isEmpty(classDescription.getFieldsMap())) {
            return;
        }
        classDescription.getFieldsMap().values().parallelStream()
                .filter(Objects::nonNull)
                .filter(fieldDesc -> fieldDesc.hasAnnotation(CvtField.class))
                .forEach(fieldDesc -> {
                    CvtField cvtField = fieldDesc.getAnnotation(CvtField.class);
                    if (null == cvtField || cvtField.sourceField().length < 1) {
                        return;
                    }
                    String[] sourceField = cvtField.sourceField();
                    Map<String, Object> valueMap = new HashMap<>(sourceField.length);
                    for (String field : sourceField) {
                        valueMap.put(field, ReflectionUtils.getFieldValue(source, field));
                    }
                    if (!cvtField.customCvt()) {
                        try {
                            fieldDesc.getField().set(tClass, valueMap.get(sourceField[0]));
                        } catch (IllegalAccessException e) {
                            log.error("", e);
                        }
                    } else {
                        ClassDescription<?> cvtClassDesc;
                        if (StringUtils.isBlank(cvtField.transformClassName())) {
                            cvtClassDesc = DescriptionCacheFactory.getClassDescription(cvtField.transformClass());
                        } else {
                            cvtClassDesc = DescriptionCacheFactory.getClassDescription(cvtField.transformClassName());
                            if ( null != cvtClassDesc && !CvtTransformHandler.class.isAssignableFrom(cvtClassDesc.getTarget())) {
                                log.warn("the class named [" + cvtClassDesc.getTarget().getName() + "] is not assignable from CvtTransformHandler");
                                cvtClassDesc = null;
                            }
                        }
                        Object value = invokeConvert(cvtClassDesc, valueMap, source);
                        try {
                            fieldDesc.getField().set(tClass, value);
                        } catch (IllegalAccessException e) {
                            log.error("", e);
                        }

                    }
                });

    }

    private static <S> Object invokeConvert(ClassDescription<?> clazzDesc, Map<String, Object> valueMap, S source) {
        if (null == clazzDesc) {
            log.warn("convert class is null");
            return null;
        }
        return ReflectionUtils.invokeMethod(clazzDesc.getInstance(),
                "transform",
                new Class[]{Map.class, source.getClass()},
                new Object[]{valueMap, source}
        );
    }


}
