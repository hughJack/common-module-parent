package cn.com.flaginfo.rocketmq.boot;

import cn.com.flaginfo.rocketmq.RocketMqBoot;
import cn.com.flaginfo.rocketmq.annotation.PushTopic;
import cn.com.flaginfo.rocketmq.consumer.MqConsumerLoader;
import cn.com.flaginfo.rocketmq.consumer.RocketMqConsumerFactory;
import cn.com.flaginfo.rocketmq.domain.ActionMappingDO;
import cn.com.flaginfo.rocketmq.exception.MqRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 下午3:28
 */
@Slf4j
public class RocketMqPushConsumerBoot extends AbstractConsumerBoot {

    public RocketMqPushConsumerBoot() {
    }

    @Override
    public void stop() {
        synchronized (RocketMqPushConsumerBoot.class){
            Map<String, DefaultMQPushConsumer> mqPushHolder = RocketMqConsumerFactory.getMqPushHolder();
            if(CollectionUtils.isEmpty(mqPushHolder)){
                return;
            }
            for( String groupName : mqPushHolder.keySet() ){
                mqPushHolder.get(groupName).suspend();
                log.warn("RocketMQ push consumer shutdown, group name:{}", groupName);
            }
        }
        log.warn("All rocket mq push consumer shutdown success.");
        log.warn("RocketMQ push consumer boot shutdown finished.");
    }

    @Override
    public void restart() {
        synchronized (RocketMqPushConsumerBoot.class){
            Map<String, DefaultMQPushConsumer> mqPushHolder = RocketMqConsumerFactory.getMqPushHolder();
            if(CollectionUtils.isEmpty(mqPushHolder)){
                return;
            }
            for( String groupName : mqPushHolder.keySet() ){
                mqPushHolder.get(groupName).resume();
                log.warn("RocketMQ push consumer restart, group name:{}", groupName);
            }
        }
        log.warn("All rocket mq push consumer restart success.");
        log.warn("RocketMQ push consumer boot restart finished.");
    }

    @Override
    public void start() {
        this.bindAction();
    }

    @Override
    public void bindAction() {
        log.info("RocketMQ push boot bing action start...");
        Map<String, ActionMappingDO> action = MqConsumerLoader.getInstance().getPushTopicMapping();
        if( CollectionUtils.isEmpty(action) ){
            log.info("RocketMQ push boot bing action ");
            return;
        }
        Set<String> keys = action.keySet();
        for (String key : keys) {
            ActionMappingDO mapping = action.get(key);
            PushTopic pushTopic = mapping.getPushTopic();
            try {
                String groupName = mapping.getConsumer().group();
                if(StringUtils.isBlank(groupName)){
                    groupName = mapping.getPushTopic().group();
                }
                if(StringUtils.isBlank(groupName)){
                    groupName = this.generationConsumerGroupId(mapping);
                }
                groupName = RocketMqBoot.getConsumerId(groupName);
                DefaultMQPushConsumer pushConsumer = RocketMqConsumerFactory.getPushConsumer(mapping, groupName);
                String topicTitle = mapping.getPushTopic().title();
                log.info("RocketMQ push consumer load mapping:{}", mapping);
                log.info("RocketMQ push consumer : {}, subscribe : {}, {}", groupName, topicTitle, pushTopic.tag());
                pushConsumer.subscribe(topicTitle, "*");
            } catch (MQClientException e) {
                log.error("", e);
                throw new MqRuntimeException(e);
            }
        }
    }

}
