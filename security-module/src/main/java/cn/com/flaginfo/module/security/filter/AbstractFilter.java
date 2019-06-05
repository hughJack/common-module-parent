package cn.com.flaginfo.module.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/**
 * @author: Meng.Liu
 * @date: 2019-05-10 15:06
 */
@Slf4j
public abstract class AbstractFilter extends ServletContextSupport implements Filter {

    protected FilterConfig filterConfig;

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.setServletContext(filterConfig.getServletContext());
    }

    protected String getInitParam(String paramName) {
        FilterConfig config = this.getFilterConfig();
        if ( null != config ) {
            String paramValue = config.getInitParameter(paramName);
            return StringUtils.isBlank(paramValue) ? null : paramValue.trim();
        }
        return null;
    }

    @Override
    public final void init(FilterConfig filterConfig) throws ServletException {
        setFilterConfig(filterConfig);
        try {
            onFilterConfigSet();
        } catch (Exception e) {
            if (e instanceof ServletException) {
                throw (ServletException) e;
            } else {
                if (log.isErrorEnabled()) {
                    log.error("Unable to start Filter: [" + e.getMessage() + "].", e);
                }
                throw new ServletException(e);
            }
        }
    }

    protected void onFilterConfigSet() throws Exception {
    }

    @Override
    public void destroy() {
    }
}
