package cn.com.flaginfo.rocketmq.message;


import cn.com.flaginfo.rocketmq.config.ConsumerType;
import cn.com.flaginfo.rocketmq.config.MqType;
import cn.com.flaginfo.rocketmq.exception.MqRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 上午10:51
 */
@Slf4j
public class RocketMqMessageAdapter extends AbstractMqMessageAdapter<ConsumeConcurrentlyStatus> implements MessageListenerConcurrently {

    /**
     * 消息模式
     */
    private final ConsumerType consumerType;

    public RocketMqMessageAdapter(ConsumerType consumerType) {
        this.consumerType = consumerType;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> exts, ConsumeConcurrentlyContext context) {
        if (exts.size() > 1) {
            throw new MqRuntimeException("mq receive message size > 1, please check the configuration.");
        }
        MessageExt ext = exts.get(0);
        this.setThreadTrace(ext.getTopic(), ext.getKeys());
        try {
            if (log.isDebugEnabled()) {
                log.debug("mq consume message enter...");
            }
            RocketMqMessage message = new RocketMqMessage(ext);
            return this.invokeMessage(consumerType, message);
        } finally {
            this.removeThreadTrace();
        }

    }

    @Override
    public ConsumeConcurrentlyStatus successType() {
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @Override
    public ConsumeConcurrentlyStatus retryType() {
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }

    @Override
    public MqType getType() {
        return MqType.rocket;
    }
}
