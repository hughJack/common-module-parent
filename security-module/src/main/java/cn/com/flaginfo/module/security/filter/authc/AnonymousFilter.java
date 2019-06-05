package cn.com.flaginfo.module.security.filter.authc;

import cn.com.flaginfo.module.security.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author: Meng.Liu
 * @date: 2019-05-10 15:40
 */
public class AnonymousFilter extends PathMatchingFilter {

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return true;
    }
}
