package cn.com.flaginfo.mongodb.config.datasource;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/12 11:24
 */
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb.first")
@ConditionalOnProperty("spring.data.mongodb.first.id")
@Order(1)
public class FirstMongoDbClient extends AbstractMongoClient {

    @Primary
    @Override
    public @Bean
    MongoTemplate instanceMongoTemplateBean() throws Exception {
        return this.instanceTemplate();
    }

    @Primary
    @Bean
    @Override
    protected MongoClient mongoClient(){
        return new MongoClient(getServerAddress(), getCredential(), mongoClientOptions());
    }
}
