package cn.com.flaginfo.mongodb.config;

import cn.com.flaginfo.module.common.AbstractMultiRouting;
import cn.com.flaginfo.mongodb.config.selector.MongodbSourceSelector;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/12 17:10
 */
@Component
@Order(2)
public class MongoDBMultiTemplateRouting extends AbstractMultiRouting<MongoTemplate> {

    @Override
    public String getMultiSourceType() {
        return MongodbSourceSelector.getInstance(false).getAndClearSelect();
    }
}
