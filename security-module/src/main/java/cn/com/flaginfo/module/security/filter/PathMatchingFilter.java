package cn.com.flaginfo.module.security.filter;

import cn.com.flaginfo.module.security.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: Meng.Liu
 * @date: 2019-05-10 15:26
 */
@Slf4j
public class PathMatchingFilter extends AdviceFilter implements PathConfigProcessor {

    /**
     * 路径匹配器
     */
    protected PathMatcher pathMatcher = new AntPathMatcher();
    /**
     * 生效的路径列表
     */
    protected Map<String, Object> appliedPaths = new LinkedHashMap<>();

    /**
     *
     * @param path
     * @param config
     * @return
     */
    @Override
    public Filter processPathConfig(String path, String config) {
        String[] values = null;
        if (config != null) {
            values = StringUtils.split(config, ",");
        }
        this.appliedPaths.put(path, values);
        return this;
    }

    /**
     * 获取应用的请求路径
     * @param request
     * @return
     */
    protected String getPathWithinApplication(ServletRequest request) {
        return WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
    }

    /**
     * 路径匹配
     * @param path
     * @param request
     * @return
     */
    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestURI = getPathWithinApplication(request);
        log.trace("Attempting to match pattern '{}' with current requestURI '{}'...", path, requestURI);
        return pathsMatch(path, requestURI);
    }

    /**
     * 路径匹配
     * @param pattern
     * @param path
     * @return
     */
    protected boolean pathsMatch(String pattern, String path) {
        return this.pathMatcher.match(pattern, path);
    }

    /**
     * 前置处理器，根据过滤器生效的url判断是否需要执行过滤器
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        if (this.appliedPaths == null || this.appliedPaths.isEmpty()) {
            if (log.isTraceEnabled()) {
                log.trace("appliedPaths property is null or empty.  This Filter will passthrough immediately.");
            }
            return true;
        }
        for (String path : this.appliedPaths.keySet()) {
            if (pathsMatch(path, request)) {
                log.trace("Current requestURI matches pattern '{}'.  Determining filter chain execution...", path);
                Object config = this.appliedPaths.get(path);
                return this.isFilterChainContinued(request, response, path, config);
            }
        }
        return true;
    }

    private boolean isFilterChainContinued(ServletRequest request, ServletResponse response,
                                           String path, Object pathConfig) throws Exception {
        if (this.isEnabled(request, response, path, pathConfig)) {
            if (log.isTraceEnabled()) {
                log.trace("Filter '{}' is enabled for the current request under path '{}' with config [{}].  " +
                                "Delegating to subclass implementation for 'onPreHandle' check.",
                        new Object[]{getName(), path, pathConfig});
            }
            return this.onPreHandle(request, response, pathConfig);
        }

        if (log.isTraceEnabled()) {
            log.trace("Filter '{}' is disabled for the current request under path '{}' with config [{}].  " +
                            "The next element in the FilterChain will be called immediately.",
                    new Object[]{getName(), path, pathConfig});
        }
        return true;
    }

    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return true;
    }

    protected boolean isEnabled(ServletRequest request, ServletResponse response, String path, Object mappedValue)
            throws Exception {
        return this.isEnabled(request, response);
    }
}
