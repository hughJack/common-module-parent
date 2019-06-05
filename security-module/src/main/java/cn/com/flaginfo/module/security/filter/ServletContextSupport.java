package cn.com.flaginfo.module.security.filter;

import javax.servlet.ServletContext;

/**
 * @author meng.liu
 */
public class ServletContextSupport {

    private ServletContext servletContext = null;

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    protected String getContextInitParam(String paramName) {
        return getServletContext().getInitParameter(paramName);
    }

    private ServletContext getRequiredServletContext() {
        ServletContext servletContext = getServletContext();
        if (servletContext == null) {
            String msg = "ServletContext property must be set via the setServletContext method.";
            throw new IllegalStateException(msg);
        }
        return servletContext;
    }

    protected void setContextAttribute(String key, Object value) {
        if (value == null) {
            removeContextAttribute(key);
        } else {
            getRequiredServletContext().setAttribute(key, value);
        }
    }

    protected Object getContextAttribute(String key) {
        return getRequiredServletContext().getAttribute(key);
    }

    protected void removeContextAttribute(String key) {
        getRequiredServletContext().removeAttribute(key);
    }
}