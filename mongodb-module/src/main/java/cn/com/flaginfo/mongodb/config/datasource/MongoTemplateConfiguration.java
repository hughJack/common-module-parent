package cn.com.flaginfo.mongodb.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * @author: Meng.Liu
 * @date: 2018/11/14 下午4:50
 */
@Configuration
@EnableMongoAuditing
@Slf4j
@Order(0)
public class MongoTemplateConfiguration {

    MongoTemplateConfiguration(){
        log.info("init spring data mongodb configuration...");
    }

}
