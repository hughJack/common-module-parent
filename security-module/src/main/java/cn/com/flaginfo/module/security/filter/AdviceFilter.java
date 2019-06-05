package cn.com.flaginfo.module.security.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 推荐的默认过滤器
 * @author: Meng.Liu
 * @date: 2019-05-10 15:20
 */
@Slf4j
public class AdviceFilter extends AbstractOncePerRequestFilter{

    /**
     * 过滤器前置方法，返回false将不再执行后续过滤链
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        return true;
    }

    /**
     * 核心过滤方法
     * @param request
     * @param response
     * @throws Exception
     */
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
    }

    /**
     * 过滤器执行完成的后置方法
     * @param request
     * @param response
     * @param exception
     * @throws Exception
     */
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
    }

    /**
     * 向下执行过滤链
     * @param request
     * @param response
     * @param chain
     * @throws Exception
     */
    protected void executeChain(ServletRequest request, ServletResponse response, FilterChain chain) throws Exception {
        chain.doFilter(request, response);
    }

    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        Exception exception = null;
        try {
            boolean continueChain = this.preHandle(request, response);
            if (log.isTraceEnabled()) {
                log.trace("Invoked preHandle method.  Continuing chain?: [" + continueChain + "]");
            }
            //如果前置过滤链返回false，表示结束过滤链
            if (continueChain) {
                this.executeChain(request, response, chain);
            }
            this.postHandle(request, response);
            if (log.isTraceEnabled()) {
                log.trace("Successfully invoked postHandle method");
            }
        } catch (Exception e) {
            exception = e;
        } finally {
            this.cleanup(request, response, exception);
        }
    }

    /**
     * 清理业务
     * @param request
     * @param response
     * @param existing
     * @throws ServletException
     * @throws IOException
     */
    protected void cleanup(ServletRequest request, ServletResponse response, Exception existing)
            throws ServletException, IOException {
        Exception exception = existing;
        try {
            this.afterCompletion(request, response, exception);
            if (log.isTraceEnabled()) {
                log.trace("Successfully invoked afterCompletion method.");
            }
        } catch (Exception e) {
            if (exception == null) {
                exception = e;
            } else {
                log.debug("afterCompletion implementation threw an exception.  This will be ignored to " +
                        "allow the original source exception to be propagated.", e);
            }
        }
        if (exception != null) {
            if (exception instanceof ServletException) {
                throw (ServletException) exception;
            } else if (exception instanceof IOException) {
                throw (IOException) exception;
            } else {
                if (log.isDebugEnabled()) {
                    String msg = "Filter execution resulted in an unexpected Exception " +
                            "(not IOException or ServletException as the Filter API recommends).  " +
                            "Wrapping in ServletException and propagating.";
                    log.debug(msg);
                }
                throw new ServletException(exception);
            }
        }
    }
}
