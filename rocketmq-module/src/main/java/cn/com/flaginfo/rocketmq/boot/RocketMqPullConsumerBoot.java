package cn.com.flaginfo.rocketmq.boot;

import cn.com.flaginfo.rocketmq.RocketMqBoot;
import cn.com.flaginfo.rocketmq.consumer.MqConsumerLoader;
import cn.com.flaginfo.rocketmq.consumer.RocketMqConsumerFactory;
import cn.com.flaginfo.rocketmq.domain.ActionMappingDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.MQPullConsumerScheduleService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 下午3:28
 */
@Slf4j
public class RocketMqPullConsumerBoot extends AbstractConsumerBoot {

    public RocketMqPullConsumerBoot() {
    }

    @Override
    public void stop() {
        synchronized (RocketMqPushConsumerBoot.class) {
            Map<String, MQPullConsumerScheduleService> mqPullHolder = RocketMqConsumerFactory.getMqPullHolder();
            if(CollectionUtils.isEmpty(mqPullHolder)){
                return;
            }
            for( String groupName : mqPullHolder.keySet() ){
                mqPullHolder.get(groupName).shutdown();
                log.warn("RocketMQ pull consumer shutdown, group name:{}", groupName);
            }
        }
        log.warn("All rocket mq pull consumer shutdown success.");
        log.warn("RocketMQ pull consumer boot shutdown finished.");
    }

    @Override
    public void restart() {
        synchronized (RocketMqPullConsumerBoot.class) {
            this.stop();
            Map<String, MQPullConsumerScheduleService> mqPullHolder = RocketMqConsumerFactory.getMqPullHolder();
            if(CollectionUtils.isEmpty(mqPullHolder)){
                return;
            }
            for( String groupName : mqPullHolder.keySet() ){
                try {
                    mqPullHolder.get(groupName).start();
                } catch (MQClientException e) {
                    log.error("restart consumer:{} error.", groupName, e);
                }
                log.warn("RocketMQ pull consumer restart, group name:{}", groupName);
            }
        }
        log.warn("All rocket mq pull consumer restart success.");
        log.warn("RocketMQ pull consumer boot restart finished.");
    }

    @Override
    public void start() {
        this.bindAction();
    }

    @Override
    public void bindAction() {
        log.info("RocketMQ pull  boot bing action start...");
        Map<String, ActionMappingDO> action = MqConsumerLoader.getInstance().getPullTopicMapping();
        if( CollectionUtils.isEmpty(action) ){
            return;
        }
        Set<String> keys = action.keySet();
        for (String key : keys) {
            ActionMappingDO mapping = action.get(key);
            String groupName = mapping.getConsumer().group();
            if(StringUtils.isBlank(groupName)){
                groupName = mapping.getPullTopic().group();
            }
            if(StringUtils.isBlank(groupName)){
                groupName = this.generationConsumerGroupId(mapping);
            }
            groupName = RocketMqBoot.getConsumerId(groupName);
            MQPullConsumerScheduleService scheduleService = RocketMqConsumerFactory.getPullConsumer(mapping, groupName);
            scheduleService.registerPullTaskCallback(mapping.getPullTopic().title(), new SechdulePullTask());
        }
    }
}
