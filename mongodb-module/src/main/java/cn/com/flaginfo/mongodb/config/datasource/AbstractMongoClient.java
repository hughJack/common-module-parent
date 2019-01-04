package cn.com.flaginfo.mongodb.config.datasource;

import cn.com.flaginfo.mongodb.config.MongoDBMultiTemplateRouting;
import cn.com.flaginfo.mongodb.config.properties.MongoDBProperties;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/12 9:42
 */
@Slf4j
public abstract class AbstractMongoClient extends MongoDBProperties {

    @Autowired
    private MongoDBMultiTemplateRouting mongoDBMultiTemplateRouting;

    public MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(mongoClient(), this.getDatabase());
    }

    protected MongoCredential getCredential(){
        return MongoCredential.createCredential(
                this.getUsername(),
                this.getDatabase(),
                this.getPassword());
    }

    protected List<ServerAddress> getServerAddress(){
        String host = this.getHost();
        if (StringUtils.isEmpty(host)) {
            throw new IllegalArgumentException(this.getId() + " MongoDB host is empty.");
        }
        String[] hosts = host.split(",");
        String[] address;
        List<ServerAddress> addresses = new ArrayList<ServerAddress>();
        for (String h : hosts) {
            if (StringUtils.isBlank(h)) {
                log.warn(this.getId() + " the mongodb host [{}] is empty, continue init this host.", h);
                continue;
            }
            h = h.trim();
            if (h.contains(":")) {
                address = h.split(":");
                log.info(this.getId() + " Init MongoDB : host [{}], port [{}]", address[0], address[1]);
                addresses.add(new ServerAddress(address[0], Integer.valueOf(address[1])));
            } else {
                log.info(this.getId() + " Init MongoDB : host [{}], no port.", h);
                addresses.add(new ServerAddress(h));
            }
        }
        return addresses;
    }

    protected MongoClient mongoClient(){
        return new MongoClient(getServerAddress(), getCredential(), mongoClientOptions());
    }

    protected MongoClientOptions mongoClientOptions() {
        MongoClientOptions.Builder mongoClientOptionsBuild = MongoClientOptions.builder();
        return mongoClientOptionsBuild.build();
    }

    protected void registerMongoTemplate(MongoTemplate mongoTemplate) throws IllegalAccessException{
        mongoDBMultiTemplateRouting.registerTemplate(this.getId(), mongoTemplate);
        if( this.isDefault() ){
            mongoDBMultiTemplateRouting.registerDefault(mongoTemplate);
        }
    }

    protected MongoTemplate instanceTemplate() throws Exception{
        MongoTemplate mongoTemplate = new MongoTemplate(this.mongoDbFactory());
        this.registerMongoTemplate(mongoTemplate);
        return mongoTemplate;
    }


    /**
     * 针对数据源生成数据源实例对象
     * @return
     * @throws Exception
     */
    abstract public MongoTemplate instanceMongoTemplateBean() throws Exception;

}
