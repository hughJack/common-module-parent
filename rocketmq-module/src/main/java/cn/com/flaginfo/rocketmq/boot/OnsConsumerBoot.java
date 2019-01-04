package cn.com.flaginfo.rocketmq.boot;

import cn.com.flaginfo.rocketmq.MqConsumerContext;
import cn.com.flaginfo.rocketmq.config.ConsumerType;
import cn.com.flaginfo.rocketmq.consumer.OnsMqConsumerFactory;
import cn.com.flaginfo.rocketmq.domain.ActionMappingDO;
import cn.com.flaginfo.rocketmq.message.OnsMqMessageAdapter;
import com.aliyun.openservices.ons.api.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 上午10:42
 */
@Slf4j
public class OnsConsumerBoot extends AbstractConsumerBoot {

    public OnsConsumerBoot() {

    }

    @Override
    public void stop() {
        synchronized (OnsConsumerBoot.class){
            Map<String, Consumer> consumers = OnsMqConsumerFactory.getMqHolder();
            if(CollectionUtils.isEmpty(consumers)){
                return;
            }
            for (String groupName : consumers.keySet()) {
                Consumer consumer = consumers.get(groupName);
                if(!consumer.isClosed()){
                    consumers.get(groupName).shutdown();
                    log.warn("Ons consumer shutdown, group name:{}", groupName);
                }
            }
            log.warn("All ons consumer shutdown success.");
            log.warn("Ons consumer boot shutdown finished.");
        }
    }

    @Override
    public void restart() {
        synchronized (OnsConsumerBoot.class){
            this.stop();
            Map<String, Consumer> consumers = OnsMqConsumerFactory.getMqHolder();
            if(CollectionUtils.isEmpty(consumers)){
                return;
            }
            for (String groupName : consumers.keySet()) {
                Consumer consumer = consumers.get(groupName);
                if(consumer.isClosed()){
                    consumers.get(groupName).start();
                    log.warn("Ons consumer restart, group name:{}", groupName);
                }
            }
            log.warn("All ons consumer restart success.");
            log.warn("Ons consumer boot restart finished.");
        }
    }

    @Override
    public void start() {
        this.bindAction();
    }

    @Override
    public void bindAction() {
        log.info("Ons consumer boot bing action start...");
        Map<String, ActionMappingDO> action = MqConsumerContext.getInstance().getAllTopicMapping();
        if( CollectionUtils.isEmpty(action) ){
            return;
        }
        Set<String> keys = action.keySet();
        for (String key : keys) {
            ActionMappingDO mapping = action.get(key);
            String groupName = mapping.getConsumer().group();
            String tag = "*";
            if(StringUtils.isBlank(groupName)){
                groupName = mapping.getPushTopic().group();
                tag = mapping.getPushTopic().tag();
            }
            if(StringUtils.isBlank(groupName)){
                groupName = mapping.getPullTopic().group();
                tag = mapping.getPullTopic().tag();
            }
            if(StringUtils.isBlank(groupName)){
                groupName = this.generationConsumerGroupId(mapping);
            }
            groupName = this.getConsumerId(groupName);
            Consumer pushConsumer = OnsMqConsumerFactory.getConsumer(mapping, groupName);
            String topicTitle = mapping.getPushTopic().title();
            if( StringUtils.isBlank(topicTitle) ){
                topicTitle = mapping.getPullTopic().title();
            }
            log.info("Ons consumer load mapping:{}", mapping);
            log.info("Ons consumer : {} subscribe : {}, {}", groupName, topicTitle, tag);
            pushConsumer.subscribe(topicTitle, "*", new OnsMqMessageAdapter(ConsumerType.Compatible));
        }
    }
}
