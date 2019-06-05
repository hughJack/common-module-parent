package cn.com.flaginfo.module.security.filter.manager;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import java.util.List;

/**
 * @author meng.liu
 */
public interface NamedFilterList extends List<Filter> {

    String getName();

    FilterChain proxy(FilterChain filterChain);
}