package cn.com.flaginfo.module.security.filter.manager;

import cn.com.flaginfo.module.security.exception.config.ConfigurationException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import java.util.Map;
import java.util.Set;

/**
 * 过滤链处理器
 * @author meng.liu
 */
public interface FilterChainManager {

    /**
     * 获取所有过滤器
     * @return
     */
    Map<String, Filter> getFilters();

    /**
     * 根据名称获取过滤器列表
     * @param chainName
     * @return
     */
    NamedFilterList getChain(String chainName);

    /**
     * 是否存在过滤链
     * @return
     */
    boolean hasChains();

    /**
     * 过滤器链名称集合
     * @return
     */
    Set<String> getChainNames();

    /**
     * 代理
     * @param original
     * @param chainName
     * @return
     */
    FilterChain proxy(FilterChain original, String chainName);

    /**
     * 增加过滤器
     * @param name
     * @param filter
     */
    void addFilter(String name, Filter filter);

    /**
     * 增加过滤器
     * @param name
     * @param filter
     * @param init
     */
    void addFilter(String name, Filter filter, boolean init);

    /**
     * 创建链
     * @param chainName
     * @param chainDefinition
     */
    void createChain(String chainName, String chainDefinition);

    /**
     * 将过滤器增加至过滤链
     * @param chainName
     * @param filterName
     */
    void addToChain(String chainName, String filterName);

    /**
     * 将过滤器增加至过滤链
     * @param chainName
     * @param filterName
     * @param chainSpecificFilterConfig
     * @throws ConfigurationException
     */
    void addToChain(String chainName, String filterName, String chainSpecificFilterConfig) throws ConfigurationException;
}