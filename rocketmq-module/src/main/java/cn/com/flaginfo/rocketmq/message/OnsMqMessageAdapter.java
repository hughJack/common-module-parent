package cn.com.flaginfo.rocketmq.message;

import cn.com.flaginfo.rocketmq.config.ConsumerType;
import cn.com.flaginfo.rocketmq.config.MqType;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 上午10:52
 */
@Slf4j
public class OnsMqMessageAdapter extends AbstractMqMessageAdapter<Action> implements MessageListener {

    /**
     * 消息模式
     */
    private final ConsumerType consumerType;

    public OnsMqMessageAdapter(ConsumerType consumerType){
        this.consumerType = consumerType;
    }

    @Override
    public Action consume(Message ext, ConsumeContext context) {
        this.setThreadTrace(ext.getTopic(), ext.getKey());
        try{
            if( log.isDebugEnabled() ){
                log.debug("mq consume message enter...");
            }
            OnsMqMessage message = new OnsMqMessage(ext);
            return this.invokeMessage(consumerType, message);
        }finally {
            this.removeThreadTrace();
        }
    }

    @Override
    public Action successType() {
        return Action.CommitMessage;
    }

    @Override
    public Action retryType() {
        return Action.ReconsumeLater;
    }

    @Override
    public MqType getType() {
        return MqType.ons;
    }
}
