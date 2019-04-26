package cn.com.flaginfo.redis;

import cn.com.flaginfo.module.common.AbstractMultiRouting;
import cn.com.flaginfo.redis.config.selector.RedisSourceSelector;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/15 16:41
 */
@Component
@Order(2)
public class RedisMultiTemplateRouting extends AbstractMultiRouting<RedisTemplate<String, Object>> {

    @Override
    public String getMultiSourceType() {
        return RedisSourceSelector.getInstance(false).getAndClearSelect();
    }
}
