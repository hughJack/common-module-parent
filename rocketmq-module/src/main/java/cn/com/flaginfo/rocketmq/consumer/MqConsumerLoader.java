package cn.com.flaginfo.rocketmq.consumer;

import cn.com.flaginfo.rocketmq.annotation.MqConsumer;
import cn.com.flaginfo.rocketmq.annotation.PullTopic;
import cn.com.flaginfo.rocketmq.annotation.PushTopic;
import cn.com.flaginfo.rocketmq.config.ConsumerType;
import cn.com.flaginfo.rocketmq.domain.ActionMappingDO;
import cn.com.flaginfo.rocketmq.exception.MqRuntimeException;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 下午2:38
 */
@Slf4j
@Getter
public class MqConsumerLoader {

    private Map<String, ActionMappingDO> pushTopicMapping = new ConcurrentHashMap<>();

    private Map<String, ActionMappingDO> pullTopicMapping = new ConcurrentHashMap<>();

    private volatile Map<String, ActionMappingDO> allTopicMapping = null;

    private MqConsumerLoader() {
    }

    /**
     * 单例
     *
     * @return
     */
    public static MqConsumerLoader getInstance() {
        return MqConsumerContextSingle.INSTANCE;
    }

    private static class MqConsumerContextSingle {
        private static final MqConsumerLoader INSTANCE = new MqConsumerLoader();
    }

    public Map<String, ActionMappingDO> getAllTopicMapping() {
        if (null == allTopicMapping) {
            synchronized (MqConsumerLoader.class) {
                if (null == allTopicMapping) {
                    allTopicMapping = new ConcurrentHashMap<>(pushTopicMapping.size()
                            + pullTopicMapping.size());
                    if (!CollectionUtils.isEmpty(pushTopicMapping)) {
                        allTopicMapping.putAll(pushTopicMapping);
                    }
                    if (!CollectionUtils.isEmpty(pullTopicMapping)) {
                        allTopicMapping.putAll(pullTopicMapping);
                    }
                }
            }
        }
        return allTopicMapping;
    }

    /**
     * 获取指定的行为
     *
     * @param topicName
     * @param tagName
     * @return
     */
    public ActionMappingDO getConsumerActionMapping(ConsumerType consumerType, String topicName, String tagName) {
        ActionMappingDO action;
        switch (consumerType) {
            case Pull:
                return this.getPullMapping(topicName, tagName);
            case Push:
                return this.getPushMapping(topicName, tagName);
            default:
                action = this.getPushMapping(topicName, tagName);
                if (null == action) {
                    action = this.getPullMapping(topicName, tagName);
                }
                return action;
        }

    }

    private ActionMappingDO getPushMapping(String topicName, String tagName) {
        ActionMappingDO action;
        if (StringUtils.isBlank(tagName)) {
            action = pushTopicMapping.get(topicName + "||*");
        } else {
            action = pushTopicMapping.get(topicName + "||" + tagName);
        }
        if (action == null && !StringUtils.isBlank(tagName)) {
            action = pushTopicMapping.get(topicName);
        }
        return action;
    }

    private ActionMappingDO getPullMapping(String topicName, String tagName) {
        ActionMappingDO action;
        if (StringUtils.isBlank(tagName)) {
            action = pullTopicMapping.get(topicName + "||*");
        } else {
            action = pullTopicMapping.get(topicName + "||" + tagName);
        }
        if (action == null && !StringUtils.isBlank(tagName)) {
            action = pullTopicMapping.get(topicName);
        }
        return action;
    }

    /**
     * 添加消费监听事件
     *
     * @param classList
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void loadPushConsumerActionWithClass(List<Class<?>> classList) {
        for (Class<?> clazz : classList) {
            Method[] methods = clazz.getMethods();

            MqConsumer consumer = clazz.getAnnotation(MqConsumer.class);
            if (consumer == null) {
                continue;
            }
            for (final Method method : methods) {
                final PushTopic topic = method.getAnnotation(PushTopic.class);
                if (topic == null) {
                    continue;
                }
                String tagName = topic.tag();
                if (StringUtils.isBlank(tagName)) {
                    tagName = "";
                }
                ActionMappingDO action = new ActionMappingDO();
                action.setConsumer(consumer);
                action.setConsumerClass(clazz);
                action.setMethod(method);
                action.setPushTopic(topic);

                String[] mTags = tagName.split("\\|\\|");
                if (StringUtils.isBlank(tagName) || mTags.length == 0) {
                    this.registerPushMapping(topic.title(), action);
                } else {
                    for (String tag : mTags) {
                        this.registerPushMapping(topic.title() + "||" + tag, action);
                    }
                }

            }
        }

        log.info("load push topic mapping success, size : {}, {}",
                pushTopicMapping.size(),
                JSONObject.toJSONString(pushTopicMapping));
    }

    private void registerPushMapping(String key, ActionMappingDO action) {
        if (pushTopicMapping.keySet().contains(key)) {
            throw new MqRuntimeException(action + "==>"
                    + key + " push register double.");
        }
        pushTopicMapping.put(key, action);
    }

    /**
     * 添加消费监听事件
     *
     * @param classList
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void loadPullConsumerActionWithClass(List<Class<?>> classList) {
        for (Class<?> clazz : classList) {
            Method[] ms = clazz.getMethods();
            MqConsumer consumer = clazz.getAnnotation(MqConsumer.class);
            if (consumer == null) {
                continue;
            }
            for (final Method m : ms) {
                final PullTopic topic = m.getAnnotation(PullTopic.class);
                if (null == topic || StringUtils.isBlank(topic.title())) {
                    continue;
                }
                ActionMappingDO action = new ActionMappingDO();
                action.setConsumerClass(clazz);
                action.setMethod(m);
                action.setPullTopic(topic);
                pullTopicMapping.put(topic.title(), action);
            }
        }
        log.info("load pull topic mapping success, size : {}, {}",
                pullTopicMapping.size(),
                JSONObject.toJSONString(pullTopicMapping));
    }

}
