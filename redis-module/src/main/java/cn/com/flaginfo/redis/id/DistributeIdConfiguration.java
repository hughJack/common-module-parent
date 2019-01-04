package cn.com.flaginfo.redis.id;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Meng.Liu
 * @date: 2018/11/20 上午10:11
 */
@Configuration
@ConditionalOnExpression("${enable.distribute.id.generator:false}")
public class DistributeIdConfiguration {


    @Bean
    public  DistributeIdGenerator initDistributeIdGenerator(){
        return new DistributeIdGenerator();
    }

}
