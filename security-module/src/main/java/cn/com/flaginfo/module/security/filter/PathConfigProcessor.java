package cn.com.flaginfo.module.security.filter;

import javax.servlet.Filter;

/**
 * 路径配置过滤器链
 */
public interface PathConfigProcessor {
    /**
     * 路径配置
     * @param path
     * @param config
     * @return
     */
    Filter processPathConfig(String path, String config);
}