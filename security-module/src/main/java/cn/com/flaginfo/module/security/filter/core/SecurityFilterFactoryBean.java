package cn.com.flaginfo.module.security.filter.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.servlet.Filter;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: Meng.Liu
 * @date: 2019-05-10 14:34
 */
@Slf4j
public class SecurityFilterFactoryBean implements FactoryBean<SpringSecurityFilter>, BeanPostProcessor {

    private SpringSecurityFilter springSecurityFilter;

    private Map<String, Filter> filters;

    private Map<String, String> filterChainDefinitionMap;

    /**
     * 针对restful请求，未登录时的响应, 默认返回状态码
     */
    private Serializable unLoginResponse = -401L;
    /**
     * 没有权限时的响应，默认返回状态码
     */
    private Serializable unauthorizedResponse = -402L;

    public SecurityFilterFactoryBean(){
        this.filters = new LinkedHashMap<>();
        this.filterChainDefinitionMap = new LinkedHashMap<>();
    }


    @Override
    public SpringSecurityFilter getObject() throws Exception {
        if( null == springSecurityFilter ){
            springSecurityFilter = this.initSpringSecurityFilter();
        }
        return null;
    }

    private SpringSecurityFilter initSpringSecurityFilter() {
        return new SpringSecurityFilter();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    public Map<String, Filter> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Filter> filters) {
        this.filters = filters;
    }

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }

    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }

    public Serializable getUnLoginResponse() {
        return unLoginResponse;
    }

    public void setUnLoginResponse(Serializable unLoginResponse) {
        this.unLoginResponse = unLoginResponse;
    }

    public Serializable getUnauthorizedResponse() {
        return unauthorizedResponse;
    }

    public void setUnauthorizedResponse(Serializable unauthorizedResponse) {
        this.unauthorizedResponse = unauthorizedResponse;
    }
}
