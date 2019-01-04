package cn.com.flaginfo.redis.cache;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Redis缓存key的生成器
 *
 * @author meng.liu
 */
@Setter
@Getter
public class RedisCacheKeyGenerator {

    private static Pattern PATTERN = Pattern.compile("(\\{([^\\}]+)\\})");

    private String cacheKey;
    /**
     * 参数名称
     */
    private String[] argsNames;
    /**
     * 参数值
     */
    private Object[] argsValues;
    /**
     * 方法参数集合
     */
    private Map<String, Object> args;

    public String generate() {
        if (this.args == null && this.argsNames != null) {
            this.args = new HashMap<>(argsNames.length);
            for (int i = 0; i < argsNames.length; i++) {
                this.args.put(argsNames[i], argsValues[i]);
            }
        }
        String cacheKey = this.cacheKey;
        Matcher matcher = PATTERN.matcher(cacheKey);
        while (matcher.find()) {
            String elValue = matcher.group(2);
            Object value = this.getValue(elValue);
            cacheKey = cacheKey.replaceAll("\\{" + elValue + "\\}", String.valueOf(value));
        }
        return cacheKey;
    }

    /**
     * 获取el表达式的值
     *
     * @param elValue
     * @return
     */
    private Object getValue(String elValue) {
        if (StringUtils.isNumeric(elValue)) {
            return this.argsValues[Integer.valueOf(elValue)];
        }
        String[] els = elValue.split("\\.");
        Object obj = this.args.get(els[0]);
        for (int i = 0; i < els.length - 1; i++) {
            if (obj == null) {
                return null;
            }
            obj = BeanUtils.getPropertyDescriptor(obj.getClass(), els[i + 1]);
        }
        return obj;
    }


}
