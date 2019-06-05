package cn.com.flaginfo.rocketmq;

import cn.com.flaginfo.module.common.diamond.DiamondProperties;
import cn.com.flaginfo.module.common.utils.ClassScannerUtils;
import cn.com.flaginfo.rocketmq.boot.OnsConsumerBoot;
import cn.com.flaginfo.rocketmq.boot.RocketMqPullConsumerBoot;
import cn.com.flaginfo.rocketmq.boot.RocketMqPushConsumerBoot;
import cn.com.flaginfo.rocketmq.config.MqType;
import cn.com.flaginfo.rocketmq.config.OnsMqConfig;
import cn.com.flaginfo.rocketmq.config.RocketMqConfig;
import cn.com.flaginfo.rocketmq.consumer.MqConsumerLoader;
import cn.com.flaginfo.rocketmq.exception.MqRuntimeException;
import cn.com.flaginfo.rocketmq.producer.OnsMqProducer;
import cn.com.flaginfo.rocketmq.producer.RocketMqProducer;
import cn.com.flaginfo.rocketmq.producer.RocketMqTemplate;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;


/**
 * 消费者工厂类
 *
 * @author: Meng.Liu
 * @date: 2018/11/29 上午10:56
 */
@Configuration
@Slf4j
@Setter
public class RocketMqAutoConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    @Value("spring.application.name:Unnamed-Service")
    private String applicationName;

    /**
     * Mq类型
     */
    private MqType type;

    @Bean
    @ConditionalOnProperty(prefix = "spring.mq.rocket", name = "address")
    public RocketMqTemplate rocketMqProducer(@Autowired RocketMqConfig rocketMqConfig) {
        this.type = MqType.rocket;
        RocketMqProducer rocketMqTemplate = new RocketMqProducer(rocketMqConfig);
        rocketMqTemplate.init();
        return rocketMqTemplate;
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.mq.ons", name = "address")
    public RocketMqTemplate onsMqProducer(@Autowired OnsMqConfig onsMqConfig) {
        this.type = MqType.ons;
        OnsMqProducer rocketMqTemplate = new OnsMqProducer(onsMqConfig, applicationName);
        rocketMqTemplate.init();
        return rocketMqTemplate;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if( null == this.type ){
            return;
        }
        if (null != contextRefreshedEvent.getApplicationContext().getParent()) {
            return;
        }
        this.startMqContext();
    }

    private void startMqContext() {
        log.info("start init MQ context...");
        String basePackage = null;
        try {
            basePackage = DiamondProperties.getPropertyString("mq.scan.base-package");
        }catch (Exception e){}
        try {
            List<Class<?>> classList = ClassScannerUtils.scanner(basePackage);
            MqConsumerLoader.getInstance().loadPushConsumerActionWithClass(classList);
            MqConsumerLoader.getInstance().loadPullConsumerActionWithClass(classList);
            switch (this.type) {
                case rocket:
                    log.info("start Rocket Mq...");
                    new RocketMqPullConsumerBoot().start();
                    new RocketMqPushConsumerBoot().start();
                    break;
                case ons:
                    log.info("start Ons Mq...");
                    new OnsConsumerBoot().start();
                    break;
                default:
                    throw new MqRuntimeException("unknown ma type.");
            }
        } catch (Exception e) {
            throw new MqRuntimeException(e);
        }
    }



}
