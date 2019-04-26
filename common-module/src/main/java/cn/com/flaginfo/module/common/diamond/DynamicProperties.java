package cn.com.flaginfo.module.common.diamond;

import ch.qos.logback.classic.Level;
import cn.com.flaginfo.module.common.utils.LogUtils;
import cn.com.flaginfo.module.common.utils.ThreadMdcUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Meng.Liu
 * @create 2017-09-26 10:55
 **/

public abstract class DynamicProperties {

    protected DynamicProperties() {
        dynamicProperties = this;
        this.setLogInfo();
    }

    private void setLogInfo() {
        LogUtils.setLogger("org.apache.commons.httpclient", Level.INFO);
        LogUtils.setLogger("httpclient.wire", Level.INFO);
        LogUtils.setLogger("httpclient.wire.header", Level.INFO);
        LogUtils.setLogger("httpclient.wire.content", Level.INFO);
        LogUtils.setLogger("com.taobao.diamond.client.impl.DefaultDiamondSubscriber", Level.INFO);
    }

    private static DynamicProperties dynamicProperties;

    protected final Set<PropertyChangeListener> changeListeners = new HashSet<PropertyChangeListener>();

    protected Map<String, Object> preData = new HashMap<>();

    /**
     * 根据属性获取配置值
     *
     * @param key
     * @return
     */
    public abstract String getProperty(String key);

    /**
     * 获取所有配置
     *
     * @return
     */
    public abstract Map<String, Object> getAllConfig();

    public static void addChangeListener(PropertyChangeListener listener) {
        dynamicProperties.changeListeners.add(listener);
    }

    public void messageChange() {
        for (PropertyChangeListener cl : changeListeners) {
            String keys[] = cl.register();
            if (keys == null) {
                break;
            }
            for (String key : keys) {
                if (isChange(key)) {
                    cl.change(key, this.preData.get(key), this.getProperty(key), this.getAllConfig());
                    break;
                }
            }
        }
    }

    protected boolean isChange(String key) {
        if (this.preData == null || this.preData.size() == 0) {
            return false;
        }
        Set<String> set = this.preData.keySet();
        int oc = 0;
        for (String var : set) {
            if (var.startsWith(key)) {
                if (!this.preData.get(var).equals(this.getProperty(var))) {
                    return true;
                }
                oc++;
            }
        }
        int nc = 0;
        set = this.getAllConfig().keySet();
        for (String var : set) {
            if (var.startsWith(key)) {
                nc++;
            }
        }
        if (nc != oc) {
            return true;
        }
        return false;
    }


}