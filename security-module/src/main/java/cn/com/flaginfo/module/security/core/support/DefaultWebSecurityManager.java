package cn.com.flaginfo.module.security.core.support;

import cn.com.flaginfo.module.security.core.SessionKey;
import cn.com.flaginfo.module.security.core.SessionManager;
import cn.com.flaginfo.module.security.core.WebSecurityManager;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author: Meng.Liu
 * @date: 2019-05-10 11:41
 */
@Slf4j
public class DefaultWebSecurityManager implements WebSecurityManager {

    private ServletRequest servletRequest;
    private ServletResponse servletResponse;
    private SessionManager sessionManager;

    public DefaultWebSecurityManager(SessionManager sessionManager, ServletRequest servletRequest, ServletResponse servletResponse){
        this.sessionManager = sessionManager;
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
    }

    @Override
    public ServletRequest getRequest() {
        return this.servletRequest;
    }

    @Override
    public ServletResponse getResponse() {
        return this.servletResponse;
    }

    @Override
    public SessionManager getSessionManager() {
        return this.sessionManager;
    }

    @Override
    public SessionKey getSessionKey() {
        return null;
    }
}
