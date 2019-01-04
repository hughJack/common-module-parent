package cn.com.flaginfo.module.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Meng.Liu
 * @date: 2018/11/23 下午5:21
 */
public class ClassScannerUtils {

    private final static String RESOURCE_PATTERN = "/**/*.class";
    private final static String DEFAULT_BASE_PACKAGE = "cn.com.flaginfo";

    private static final Map<String, List<Class<?>>> SCANNER_CLASS_CACHE = new ConcurrentHashMap<>();

    public static List<Class<?>> scanner(String basePackage) throws IOException, ClassNotFoundException {
        // 扫描的包名
        if (StringUtils.isBlank(basePackage)) {
            basePackage = DEFAULT_BASE_PACKAGE;
        }
        if( SCANNER_CLASS_CACHE.containsKey(basePackage)){
            return SCANNER_CLASS_CACHE.get(basePackage);
        }
        synchronized (SCANNER_CLASS_CACHE){
            if( SCANNER_CLASS_CACHE.containsKey(basePackage)){
                return SCANNER_CLASS_CACHE.get(basePackage);
            }
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(basePackage)
                    + RESOURCE_PATTERN;
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            List<Class<?>> classes = new ArrayList<>();
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    String className = reader.getClassMetadata().getClassName();
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                }
            }
            SCANNER_CLASS_CACHE.put(basePackage, classes);
            return classes;
        }
    }
}