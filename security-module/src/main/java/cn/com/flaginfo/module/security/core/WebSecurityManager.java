package cn.com.flaginfo.module.security.core;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * web上下文
 * @author: Meng.Liu
 * @date: 2019-05-10 11:33
 */
public interface WebSecurityManager extends SecurityManager{
    /**
     * 获取servletRequest
     * @return
     */
    ServletRequest getRequest();

    /**
     * 获取servletResponse
     * @return
     */
    ServletResponse getResponse();
}
