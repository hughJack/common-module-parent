package cn.com.flaginfo.rocketmq.producer;

import cn.com.flaginfo.module.common.utils.SpringContextUtils;
import cn.com.flaginfo.rocketmq.config.OnsMqConfig;
import cn.com.flaginfo.rocketmq.domain.SendResultDO;
import cn.com.flaginfo.rocketmq.exception.MqRuntimeException;
import com.alibaba.fastjson.JSONObject;
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
public class OnsMqProducer extends AbstractMqProducer {

    private Producer producer;

    public OnsMqProducer() {
    }

    @Override
    public void init() {
        //do nothing
        throw new MqRuntimeException("please use the method `init(groupId)` to init.");
    }

    @Override
    public void init(String groupName) {
        log.info("init ons producer, producer id : {}", groupName);
        OnsMqConfig onsMqConfig = SpringContextUtils.getBean(OnsMqConfig.class);
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ONSAddr, onsMqConfig.getAddress());
        properties.put(PropertyKeyConst.AccessKey, onsMqConfig.getAccessKey());
        properties.put(PropertyKeyConst.SecretKey, onsMqConfig.getSecretKey());
        properties.put(PropertyKeyConst.ProducerId, groupName);
        properties.put(PropertyKeyConst.isVipChannelEnabled, onsMqConfig.getVipChannelEnabled());
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
