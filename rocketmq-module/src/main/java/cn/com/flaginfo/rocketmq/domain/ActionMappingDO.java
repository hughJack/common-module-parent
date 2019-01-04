package cn.com.flaginfo.rocketmq.domain;

import cn.com.flaginfo.rocketmq.annotation.MqConsumer;
import cn.com.flaginfo.rocketmq.annotation.PushTopic;
import cn.com.flaginfo.rocketmq.annotation.PullTopic;
import cn.com.flaginfo.rocketmq.exception.MqRuntimeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 下午2:31
 */
@Slf4j
@Getter
@Setter
@ToString
public class ActionMappingDO implements Serializable {

    /**
     * 可执行类对象
     */
    private static final Map<String, Object> ACTION_OBJECT = new ConcurrentHashMap<>();

    private Class<?> consumerClass;

    private MqConsumer consumer;

    private PushTopic pushTopic;

    private PullTopic pullTopic;

    private Method method;

    private Object consumerObject;

    public void setConsumerClass(Class<?> consumerClass) {
        this.consumerClass = consumerClass;
        synchronized (ACTION_OBJECT) {
            this.consumerObject = ACTION_OBJECT.get(consumerClass.getName());
            if (consumerObject == null) {
                try {
                    this.consumerObject = consumerClass.newInstance();
                    ACTION_OBJECT.put(consumerClass.getName(), consumerClass.newInstance());
                } catch (Exception e) {
                    log.error("", e);
                    throw new MqRuntimeException(e);
                }
            }
        }
    }

    public Object getTopic() {
        if (this.pushTopic != null) {
            return this.pushTopic;
        }
        return this.pullTopic;
    }

}
