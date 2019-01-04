package cn.com.flaginfo.module.common.diamond;

import java.util.Map;

/**
 * @author Meng.Liu
 * @create 2017-09-26 10:56
 **/
public abstract class PropertyChangeListener {
    /**
     * 注册监听
     */
    public PropertyChangeListener() {
        DiamondProperties.addChangeListener(this);
    }

    /**
     * 注册监听的属性的key
     * @return
     */
    public abstract String[] register();

    /**
     * 当属性值变化时执行该方法
     * @param oldValue
     * @param newValue
     * @param allConfig
     * @param key
     */
    public abstract void change(String key, Object oldValue, Object newValue, Map<String,Object> allConfig);
}