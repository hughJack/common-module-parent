package cn.com.flaginfo.rocketmq.producer;

import cn.com.flaginfo.module.common.utils.SpringContextUtils;
import cn.com.flaginfo.rocketmq.config.RocketMqConfig;
import cn.com.flaginfo.rocketmq.domain.SendResultDO;
import cn.com.flaginfo.rocketmq.exception.MqRuntimeException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 下午12:01
 */
@Slf4j
public class RocketMqProducer extends AbstractMqProducer {

    private DefaultMQProducer producer;

    public RocketMqProducer() {

    }

    @Override
    public void init() {
        try {
            RocketMqConfig rocketMqConfig = SpringContextUtils.getBean(RocketMqConfig.class);
            System.setProperty("client.logFileMaxIndex", "10");
            producer = new DefaultMQProducer();
            producer.setProducerGroup(rocketMqConfig.getProducerGroup());
            if( log.isDebugEnabled() ){
                log.debug("rocket producer name address:{}", rocketMqConfig.getAddress());
            }
            producer.setNamesrvAddr(rocketMqConfig.getAddress());
            producer.setInstanceName(getClass().getSimpleName() + hashCode());
            producer.setHeartbeatBrokerInterval(rocketMqConfig.getHeartbeatBrokerInterval());
            producer.setMaxMessageSize(rocketMqConfig.getMaxMessageSize());
            producer.setVipChannelEnabled(rocketMqConfig.getVipChannelEnabled());
            producer.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    producer.shutdown();
                    log.info("rocket mq producer shutdown");
                    super.run();
                }
            });
            if( log.isDebugEnabled() ){
                log.debug("init rocket mq success");
            }
        } catch (MQClientException e) {
            log.error("MQClientException", e);
            throw new MqRuntimeException("init rocket mq producer error, please check your config.");
        }
    }

    @Override
    public void init(String groupId) {
        init();
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
        message.setKeys(keys);
        message.setTags(tags);

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

        if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
            response.setSuccess(true);
            response.setMessageId(sendResult.getMsgId());
        }
        if( log.isDebugEnabled() ){
            log.debug("rocket mq send result:{}", sendResult);
        }
        log.info("mq send success {}, takes:{}ms", message.getTopic(), System.currentTimeMillis() - start);
        return response;
    }

}
