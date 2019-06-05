package cn.com.flaginfo.module.security.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 同类过滤器
 * @author meng.liu
 */
@Slf4j
public abstract class AbstractOncePerRequestFilter extends AbstractNameableFilter {

    /**
     * 过滤器已执行状态
     */
    public static final String ALREADY_FILTERED_SUFFIX = ".SECURITY_FILTERED";

    /**
     * 过滤器是否生效
     */
    private boolean enabled = true;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String alreadyFilteredAttributeName = this.getAlreadyFilteredAttributeName();
        if ( request.getAttribute(alreadyFilteredAttributeName) != null ) {
            log.trace("Filter '{}' already executed.  Proceeding without invoking this filter.", getName());
            filterChain.doFilter(request, response);
        } else if ( !this.isEnabled(request, response)) {
            log.debug("Filter '{}' is not enabled for the current request.  Proceeding without invoking this filter.", getName());
            filterChain.doFilter(request, response);
        } else {
            log.trace("Filter '{}' not yet executed.  Executing now.", getName());
            request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
            try {
                this.doFilterInternal(request, response, filterChain);
            } finally {
                request.removeAttribute(alreadyFilteredAttributeName);
            }
        }
    }

    /**
     * 是否生效
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    protected boolean isEnabled(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        return this.isEnabled();
    }

    /**
     * 获取同类过滤器已自行key
     * @return
     */
    protected String getAlreadyFilteredAttributeName() {
        String name = getName();
        if (name == null) {
            name = getClass().getName();
        }
        return name + ALREADY_FILTERED_SUFFIX;
    }

    /**
     * 过滤器核心类，由子类完成其业务
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    protected abstract void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException;
}
