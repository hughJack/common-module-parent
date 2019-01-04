package cn.com.flaginfo.rocketmq.producer;

import cn.com.flaginfo.module.common.utils.SpringContextUtils;
import cn.com.flaginfo.rocketmq.config.MqConfig;
import cn.com.flaginfo.rocketmq.config.MqType;
import cn.com.flaginfo.rocketmq.domain.SendResultDO;
import cn.com.flaginfo.rocketmq.exception.MqRuntimeException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Producer抽象类，调用getInstance静态方法获取实例对象发送消息
 *
 * @author: Meng.Liu
 * @date: 2018/11/22 上午11:40
 */
@Slf4j
public abstract class AbstractMqProducer {

    static final Map<MqType, AbstractMqProducer> ROCKET_MQ_PRODUCER_HOLDER = new ConcurrentHashMap<>();

    static final Map<String, AbstractMqProducer> ONS_MQ_PRODUCER_HOLDER = new ConcurrentHashMap<>();

    private static MqConfig mqConfig = null;

    /**
     * 获取单例类
     *
     * @param groupId
     * @return
     */
    protected static AbstractMqProducer getInstance(String groupId) {
        if (null == mqConfig) {
            mqConfig = SpringContextUtils.getBean(MqConfig.class);
        }
        MqType type = mqConfig.getType();
        if( log.isDebugEnabled() ){
            log.debug("mq producer type is : {}", type);
        }
        switch (type) {
            case rocket:
                if( log.isDebugEnabled() ){
                    log.debug("get rocket mq instance by type: rocket mq");
                }
                return getSingletonRocketMqInstance(MqType.rocket);
            case ons:
                if( log.isDebugEnabled() ){
                    log.debug("get ons mq instance by groupId:ons", groupId);
                }
                return getSingletonOnsMqInstance(groupId);
            default:
                log.error("unknown mq type.");
                throw new MqRuntimeException("unknown mq type.");

        }
    }

    private static AbstractMqProducer getSingletonRocketMqInstance(MqType type) {
        AbstractMqProducer p = ROCKET_MQ_PRODUCER_HOLDER.get(type);
        if (p == null) {
            synchronized (ROCKET_MQ_PRODUCER_HOLDER) {
                p = ROCKET_MQ_PRODUCER_HOLDER.get(type);
                if (p != null) {
                    return p;
                }
                if (type == MqType.rocket) {
                    p = new RocketMqProducer();
                    p.init();
                    ROCKET_MQ_PRODUCER_HOLDER.put(type, p);
                }
            }
        }
        return p;
    }

    /**
     * 多例Producer
     *
     * @param groupName 类型，选择需要的MQ类型
     * @return
     */
    private static AbstractMqProducer getSingletonOnsMqInstance(String groupName) {
        AbstractMqProducer p = ONS_MQ_PRODUCER_HOLDER.get(groupName);
        if (p == null) {
            synchronized (ONS_MQ_PRODUCER_HOLDER) {
                p = ONS_MQ_PRODUCER_HOLDER.get(groupName);
                if (p != null) {
                    return p;
                }
                p = new OnsMqProducer();
                p.init(groupName);
                ONS_MQ_PRODUCER_HOLDER.put(groupName, p);
            }
        }
        return p;
    }

    /**
     * 初始化
     */
    public abstract void init();

    /**
     * 初始化方法
     *
     * @param groupId
     */
    public abstract void init(String groupId);

    /**
     * 发送消息
     *
     * @param topicName
     * @param body
     * @return
     */
    public abstract SendResultDO sendMessage(String topicName, String body);

    /**
     * 发送消息
     *
     * @param topicName
     * @param tags      标签
     * @param keys      关键字，尽量保持唯一性，方便以后查询
     * @param body      内容
     * @return
     */
    public abstract SendResultDO sendMessage(final String topicName, final String tags, final String keys,
                                             String body);

    /**
     * 自动转JSON格式的消息
     *
     * @param topicName
     * @param body
     * @return
     */
    public SendResultDO sendJsonMessage(String topicName, final Object body) {
        return sendMessage(topicName,  JSONObject.toJSONString(body));
    }

    /**
     * 自动转JSON格式的消息
     *
     * @param topicName
     * @param body
     * @return
     */
    public SendResultDO sendJsonMessage(String topicName, final String keys, final Object body) {
        return sendMessage(topicName, "", keys, JSONObject.toJSONString(body));
    }

    /**
     * 自动转JSON格式的消息
     *
     * @param topicName
     * @param body
     * @return
     */
    public SendResultDO sendJsonMessage(String topicName, String tags, String keys, Object body) {
        return sendMessage(topicName, tags, keys, JSONObject.toJSONString(body));
    }
}
