package cn.com.flaginfo.module.security.filter.manager.support;

import cn.com.flaginfo.module.security.exception.config.InstantiationException;
import cn.com.flaginfo.module.security.filter.authc.AnonymousFilter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author meng.liu
 */

public enum DefaultFilter {

    /**
     * 直接放行所有请求
     */
    anon(AnonymousFilter.class),
    perms(PermissionsAuthorizationFilter.class),
    user(UserFilter.class);

    private final Class<? extends Filter> filterClass;

    private DefaultFilter(Class<? extends Filter> filterClass) {
        this.filterClass = filterClass;
    }

    public Filter newInstance() {
        return newInstance(this.filterClass);
    }

    public Class<? extends Filter> getFilterClass() {
        return this.filterClass;
    }

    public static Map<String, Filter> createInstanceMap(FilterConfig config) {
        Map<String, Filter> filters = new LinkedHashMap<>(values().length);
        for (DefaultFilter defaultFilter : values()) {
            Filter filter = defaultFilter.newInstance();
            if (config != null) {
                try {
                    filter.init(config);
                } catch (ServletException e) {
                    String msg = "Unable to correctly init default filter instance of type " +
                            filter.getClass().getName();
                    throw new IllegalStateException(msg, e);
                }
            }
            filters.put(defaultFilter.name(), filter);
        }
        return filters;
    }

    private static Filter newInstance(Class<? extends Filter> filterClass){
        if( null == filterClass ){
            throw new IllegalArgumentException("Class type cannot be null");
        }
        try {
            return filterClass.newInstance();
        }catch (Exception e ){
            throw new InstantiationException("Unable to instantiate class [" + filterClass.getName() + "]", e);
        }
    }
}