package cn.com.flaginfo.rocketmq.producer;

import cn.com.flaginfo.rocketmq.domain.SendResultDO;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * Producer抽象类
 *
 * @author: Meng.Liu
 * @date: 2018/11/22 上午11:40
 */
@Slf4j
public abstract class RocketMqTemplate {

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
