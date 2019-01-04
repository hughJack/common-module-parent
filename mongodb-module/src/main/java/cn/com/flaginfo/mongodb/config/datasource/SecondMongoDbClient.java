package cn.com.flaginfo.mongodb.config.datasource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/12 11:24
 */
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb.second")
@ConditionalOnProperty("spring.data.mongodb.second.id")
@Order(1)
public class SecondMongoDbClient extends AbstractMongoClient {

    @Override
    public @Bean("secondMongoTemplate")
    MongoTemplate instanceMongoTemplateBean() throws Exception {
        return this.instanceTemplate();
    }
}
