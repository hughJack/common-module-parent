package cn.com.flaginfo.module.reflect.annotation;

import java.util.Map;

/**
 * @author: Meng.Liu
 * @date: 2019/1/24 下午5:13
 */
public interface CvtTransformHandler<T> {

    /**
     * 转换器
     * @param sourceValueMap
     * @param source
     * @return
     */
    Object transform(Map<String, Object> sourceValueMap, T source);

}
