package cn.com.flaginfo.rocketmq.consumer;

import cn.com.flaginfo.module.common.utils.SpringContextUtils;
import cn.com.flaginfo.rocketmq.config.ConsumerType;
import cn.com.flaginfo.rocketmq.config.RocketMqConfig;
import cn.com.flaginfo.rocketmq.domain.ActionMappingDO;
import cn.com.flaginfo.rocketmq.message.RocketMqMessageAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPullConsumerScheduleService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 上午10:44
 */
@Slf4j
public class RocketMqConsumerFactory {


    private final static Map<String, DefaultMQPushConsumer> MQ_PUSH_HOLDER = new ConcurrentHashMap<>();

    private final static Map<String, MQPullConsumerScheduleService> MQ_PULL_HOLDER = new ConcurrentHashMap<>();

    /**
     * 按照groupName生成Consumer
     *
     * @param mappingDO
     * @param groupName
     * @return
     */
    public static synchronized DefaultMQPushConsumer getPushConsumer(ActionMappingDO mappingDO, String groupName) {
        DefaultMQPushConsumer pushConsumer = MQ_PUSH_HOLDER.get(groupName);
        if (pushConsumer != null) {
            return pushConsumer;
        }

        RocketMqConfig rocketMqConfig = SpringContextUtils.getBean(RocketMqConfig.class);
        pushConsumer = new DefaultMQPushConsumer();
        pushConsumer.setVipChannelEnabled(rocketMqConfig.getVipChannelEnabled());
        pushConsumer.setConsumerGroup(groupName);
        pushConsumer.setNamesrvAddr(rocketMqConfig.getAddress());
        log.info("init mq push consumer, address:{}, group:{}", rocketMqConfig.getAddress(), groupName);
        pushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        pushConsumer.setInstanceName(RocketMqConsumerFactory.class.hashCode() + "_push");
        pushConsumer.setMessageModel(mappingDO.getConsumer().messageModel());
        pushConsumer.setMessageListener(new RocketMqMessageAdapter(ConsumerType.Push));
        pushConsumer.setConsumeThreadMin(rocketMqConfig.getMinThread());
        pushConsumer.setConsumeThreadMax(rocketMqConfig.getMinThread());
        pushConsumer.setConsumeMessageBatchMaxSize(1);
        try {
            pushConsumer.start();
        } catch (MQClientException e) {
            log.error("start consumer error.", e);
        }
        MQ_PUSH_HOLDER.put(groupName, pushConsumer);
        return pushConsumer;
    }

    /**
     * 按照groupName生成Consumer
     *
     * @param mappingDO
     * @param groupName
     * @return
     */
    public static synchronized MQPullConsumerScheduleService getPullConsumer(ActionMappingDO mappingDO, String groupName) {
        MQPullConsumerScheduleService scheduleService = MQ_PULL_HOLDER.get(groupName);
        if (scheduleService != null) {
            return scheduleService;
        }
        RocketMqConfig rocketMqConfig = SpringContextUtils.getBean(RocketMqConfig.class);
        scheduleService = new MQPullConsumerScheduleService(groupName);
        DefaultMQPullConsumer defaultMQPullConsumer = scheduleService.getDefaultMQPullConsumer();
        defaultMQPullConsumer.setVipChannelEnabled(rocketMqConfig.getVipChannelEnabled());
        defaultMQPullConsumer.setNamesrvAddr(rocketMqConfig.getAddress());
        defaultMQPullConsumer.setMessageModel(mappingDO.getConsumer().messageModel());
        defaultMQPullConsumer.setInstanceName(RocketMqConsumerFactory.class.hashCode() + "_schedule_instance");
        scheduleService.setMessageModel(mappingDO.getConsumer().messageModel());
        scheduleService.setPullThreadNums(rocketMqConfig.getPullThreadNum());
        try {
            scheduleService.start();
        } catch (MQClientException e) {
            log.error("start consumer error.", e);
        }
        MQ_PULL_HOLDER.put(groupName, scheduleService);
        return scheduleService;
    }

    /**
     * 获取所有的DefaultMQPushConsumer对象
     *
     * @return
     */
    public static Map<String, DefaultMQPushConsumer> getMqPushHolder() {
        return MQ_PUSH_HOLDER;
    }

    /**
     * 获取所有的DefaultMQPullConsumer对象
     *
     * @return
     */
    public static Map<String, MQPullConsumerScheduleService> getMqPullHolder() {
        return MQ_PULL_HOLDER;
    }
}
