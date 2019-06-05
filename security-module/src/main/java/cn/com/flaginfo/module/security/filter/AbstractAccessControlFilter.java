package cn.com.flaginfo.module.security.filter;

import cn.com.flaginfo.module.security.utils.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 权限校验过滤器
 * @author: Meng.Liu
 * @date: 2019-05-10 15:52
 */
public abstract class AbstractAccessControlFilter extends PathMatchingFilter{

    public static final String GET_METHOD = "GET";

    public static final String POST_METHOD = "POST";

    /**
     * 未登录的返回信息
     */
    private Serializable noLoginResponse = -401;

    public Serializable getNoLoginResponse() {
        return this.noLoginResponse;
    }

    public void setNoLoginResponse(Serializable noLoginResponse) {
        this.noLoginResponse = noLoginResponse;
    }


    protected Subject getSubject(ServletRequest request, ServletResponse response) {
        return SecurityUtils.getSubject();
    }

    /**
     * 是否允许
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws Exception
     */
    protected abstract boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception;

    /**
     * 无权限访问
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws Exception
     */
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return this.onAccessDenied(request, response);
    }

    /**
     * 无权限访问的业务逻辑
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected abstract boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception;

    /**
     * 过滤器前置处理器
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws Exception
     */
    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return this.isAccessAllowed(request, response, mappedValue) || onAccessDenied(request, response, mappedValue);
    }

    protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
        return pathsMatch(getLoginUrl(), request);
    }
}
