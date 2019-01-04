package cn.com.flaginfo.rocketmq;

import cn.com.flaginfo.module.common.diamond.DiamondProperties;
import cn.com.flaginfo.module.common.utils.ClassScannerUtils;
import cn.com.flaginfo.rocketmq.boot.OnsConsumerBoot;
import cn.com.flaginfo.rocketmq.boot.RocketMqPullConsumerBoot;
import cn.com.flaginfo.rocketmq.boot.RocketMqPushConsumerBoot;
import cn.com.flaginfo.rocketmq.config.MqConfig;
import cn.com.flaginfo.rocketmq.config.MqType;
import cn.com.flaginfo.rocketmq.exception.MqRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

/**
 * @author: Meng.Liu
 * @date: 2018/11/23 下午5:13
 */
@Slf4j
@Configuration
@ConditionalOnBean(MqConfig.class)
public class MqConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MqConfig mqType;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (null != contextRefreshedEvent.getApplicationContext().getParent()) {
            return;
        }
        this.startMqContext();
    }

    private void startMqContext() {
        log.info("start init MQ context...");
        final String basePackage = DiamondProperties.getPropertyString("mq.scan.base-package");
        try {
            List<Class<?>> classList = ClassScannerUtils.scanner(basePackage);
            MqConsumerContext.getInstance().loadPushConsumerActionWithClass(classList);
            MqConsumerContext.getInstance().loadPullConsumerActionWithClass(classList);
            MqType type = mqType.getType();
            switch (type) {
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
            log.error("", e);
        }
    }

}
