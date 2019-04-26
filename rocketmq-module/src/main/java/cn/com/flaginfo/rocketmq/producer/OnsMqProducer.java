package cn.com.flaginfo.rocketmq.producer;

import cn.com.flaginfo.rocketmq.RocketMqBoot;
import cn.com.flaginfo.rocketmq.config.OnsMqConfig;
import cn.com.flaginfo.rocketmq.domain.SendResultDO;
import com.aliyun.openservices.ons.api.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 下午12:02
 */
@Slf4j
public class OnsMqProducer extends RocketMqTemplate {

    private Producer producer;

    private OnsMqConfig onsMqConfig;

    private String defaultProducerGroupName;

    public OnsMqProducer(OnsMqConfig onsMqConfig, String defaultProducerGroupName) {
        this.onsMqConfig = onsMqConfig;
        this.defaultProducerGroupName = defaultProducerGroupName;
    }

    public void init() {
        String groupName;
        if( null == this.onsMqConfig || StringUtils.isBlank(this.onsMqConfig.getProducerGroupName()) ){
            groupName = RocketMqBoot.getProducerId(defaultProducerGroupName);
        }else{
            groupName = RocketMqBoot.getProducerId(this.onsMqConfig.getProducerGroupName());
        }
        log.info("init ons producer, producer name : {}", groupName);
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.NAMESRV_ADDR, this.onsMqConfig.getAddress());
        properties.put(PropertyKeyConst.AccessKey, this.onsMqConfig.getAccessKey());
        properties.put(PropertyKeyConst.SecretKey, this.onsMqConfig.getSecretKey());
        properties.put(PropertyKeyConst.GROUP_ID, groupName);
        properties.put(PropertyKeyConst.isVipChannelEnabled, this.onsMqConfig.getVipChannelEnabled());
        producer = ONSFactory.createProducer(properties);
        producer.start();
        log.info("init ons producer success.");
    }

    @Override
    public SendResultDO sendMessage(String topicName, String body) {
        return sendMessage(topicName, "", null, body);
    }

    @Override
    public SendResultDO sendMessage(final String topicName, final String tags, final String keys, String body) {
        if (body == null) {
            body = "";
        }

        Message message = new Message();
        message.setTopic(topicName);
        message.setBody(body.getBytes(StandardCharsets.UTF_8));
        message.setKey(keys);
        message.setTag(tags);
        return sendMessage(message);
    }

    private SendResultDO sendMessage(Message message) {
        SendResultDO response = new SendResultDO();
        long start = System.currentTimeMillis();
        SendResult sendResult;
        try {
            sendResult = producer.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!StringUtils.isEmpty(sendResult.getMessageId())) {
            response.setSuccess(true);
            response.setMessageId(sendResult.getMessageId());
        }
        if( log.isDebugEnabled() ){
            log.debug("ons mq send result : {}", sendResult);
        }
        log.info("ons send success {}, takes:{}ms", message.getTopic(), System.currentTimeMillis() - start);
        return response;
    }


}
