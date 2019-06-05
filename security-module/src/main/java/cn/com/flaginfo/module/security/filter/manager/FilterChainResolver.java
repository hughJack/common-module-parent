package cn.com.flaginfo.module.security.filter.manager;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * 过滤器链解析器
 * @author meng.liu
 */
public interface FilterChainResolver {

    /**
     * 获取过滤链
     * @param request
     * @param response
     * @param originalChain
     * @return
     */
    FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain);

}