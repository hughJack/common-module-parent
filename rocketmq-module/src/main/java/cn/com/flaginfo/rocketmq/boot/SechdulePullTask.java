package cn.com.flaginfo.rocketmq.boot;

import cn.com.flaginfo.rocketmq.consumer.MqConsumerLoader;
import cn.com.flaginfo.rocketmq.domain.ActionMappingDO;
import cn.com.flaginfo.rocketmq.domain.ConsumerResultDO;
import cn.com.flaginfo.rocketmq.message.MqMessage;
import cn.com.flaginfo.rocketmq.message.RocketMqMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.*;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class SechdulePullTask implements PullTaskCallback {

    @Override
    public void doPullTask(MessageQueue mq, PullTaskContext context) {
        log.info("RocketMQ doPullTask topicName:" + mq.getTopic());
        try {
            MQPullConsumer consumer = context.getPullConsumer();
            ActionMappingDO action = MqConsumerLoader.getInstance().getPullTopicMapping().get(mq.getTopic());
            long offset = consumer.fetchConsumeOffset(mq, false);
            offset = offset < 0 ? 0 : offset;
            log.info("RocketMQ pull queue id : {} ,offset : {}", mq.getBrokerName() + "-" + mq.getQueueId(), offset);
            PullResult pullResult = consumer.pull(mq, action.getPullTopic().tag(), offset, 10000);
            if (PullStatus.FOUND == pullResult.getPullStatus()) {
                List<MessageExt> list = pullResult.getMsgFoundList();
                Method m = action.getMethod();
                ConsumerResultDO result = (ConsumerResultDO) m.invoke(action.getConsumerObject(), getMethodArgs(m, list));
                if (result != null && !result.getRetry()) {
                    log.info("RocketMQ pull update consume:{}-{}-{},nextOffset:{}", mq.getTopic(), mq.getBrokerName(), mq.getQueueId(), pullResult.getNextBeginOffset());
                    consumer.updateConsumeOffset(mq, pullResult.getNextBeginOffset());
                }
            }
            context.setPullNextDelayTimeMillis(action.getPullTopic().delay() * 1000);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 组装方法的参数
     * 可初始更多的参数，待完善
     *
     * @return
     */
    public Object[] getMethodArgs(Method m, List<MessageExt> exts) {
        Class<?>[] cs = m.getParameterTypes();
        Object[] args;
        if (cs == null || cs.length == 0) {
            return null;
        }
        log.info("getParameterTypes {} ", Arrays.asList(cs));

        List<MqMessage> msgList = new ArrayList<>(exts.size());
        for (MessageExt ext : exts) {
            MqMessage message = new RocketMqMessage(ext);
            msgList.add(message);
        }
        args = new Object[cs.length];
        int i = 0;
        for (Class c : cs) {
            if (c.isAssignableFrom(List.class)) {
                args[i] = msgList;
            } else if (c.getName().equals(ConsumerResultDO.class.getName())) {
                args[i] = new ConsumerResultDO();
            }
            i++;
        }
        return args;
    }


}