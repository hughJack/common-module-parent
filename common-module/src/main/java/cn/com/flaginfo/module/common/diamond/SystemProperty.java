package cn.com.flaginfo.module.common.diamond;

import java.util.Map;

/**
 * @author: Meng.Liu
 * @date: 2019-05-16 15:27
 */
public class SystemProperty {

    private static final DynamicProperties PROPERTIES = DynamicProperties.getInstance();

    /**
     * 获取所有配置数据
     *
     * @return
     */
    private static Map<String, Object> getAllConfig() {
        return PROPERTIES.getAllConfig();
    }

    public static Integer getInteger(String s, Integer defVal) {
        Integer val = getInteger(s);
        return null == val ? defVal : val;
    }

    public static Integer getInteger(String s) {
        try {
            return Integer.valueOf(getString(s));
        } catch (Exception e) {
            return null;
        }
    }

    public static Long getLong(String s, Long defVal) {
        Long val = getLong(s);
        return null == val ? defVal : val;
    }

    public static Long getLong(String s) {
        try {
            return Long.valueOf(getString(s));
        } catch (Exception e) {
            return null;
        }
    }

    public static Double getDouble(String s, Double defVal) {
        Double val = getDouble(s);
        return null == val ? defVal : val;
    }

    public static Double getDouble(String s) {
        try {
            return Double.valueOf(getString(s));
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean getBoolean(String s, Boolean defVal) {
        Boolean val = getBoolean(s);
        return null == val ? defVal : val;
    }

    public static Boolean getBoolean(String s) {
        try {
            return Boolean.valueOf(getString(s));
        } catch (Exception e) {
            return null;
        }
    }

    public static String getString(String s, String defVal) {
        return PROPERTIES.getProperty(s, defVal);
    }

    public static String getString(String s) {
        return PROPERTIES.getProperty(s);
    }

}
