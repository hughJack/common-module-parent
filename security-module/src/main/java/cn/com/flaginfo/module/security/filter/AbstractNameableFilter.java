package cn.com.flaginfo.module.security.filter;

import javax.servlet.FilterConfig;

/**
 * @author: Meng.Liu
 * @date: 2019-05-10 15:13
 */
public abstract class AbstractNameableFilter extends AbstractFilter implements Nameable {

    private String name;

    protected String getName() {
        if (this.name == null) {
            FilterConfig config = this.getFilterConfig();
            if (config != null) {
                this.name = config.getFilterName();
            }
        }
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
