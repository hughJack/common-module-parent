package cn.com.flaginfo.rocketmq.consumer;

import cn.com.flaginfo.module.common.utils.SpringContextUtils;
import cn.com.flaginfo.rocketmq.config.OnsMqConfig;
import cn.com.flaginfo.rocketmq.domain.ActionMappingDO;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 上午10:44
 */
@Slf4j
public class OnsMqConsumerFactory {


    private final static Map<String, Consumer> MQ_HOLDER = new ConcurrentHashMap<>();

    /**
     * 按照groupName生成Consumer
     *
     * @param groupName
     * @return
     */
    public static synchronized Consumer getConsumer(ActionMappingDO mappingDO, String groupName) {
        OnsMqConfig onsMqConfig = SpringContextUtils.getBean(OnsMqConfig.class);
        Consumer consumer = MQ_HOLDER.get(groupName);
        if (consumer != null) {
            return consumer;
        }
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.MessageModel, mappingDO.getConsumer().messageModel().getModeCN());
        properties.put(PropertyKeyConst.AccessKey, onsMqConfig.getAccessKey());
        properties.put(PropertyKeyConst.SecretKey, onsMqConfig.getSecretKey());
        properties.put(PropertyKeyConst.ONSAddr, onsMqConfig.getAddress());
        properties.put(PropertyKeyConst.ConsumerId, groupName);
        properties.put(PropertyKeyConst.isVipChannelEnabled, onsMqConfig.getVipChannelEnabled());
        properties.put(PropertyKeyConst.ConsumeThreadNums, onsMqConfig.getConsumeThreadNumber());
        consumer = ONSFactory.createConsumer(properties);
        consumer.start();
        MQ_HOLDER.put(groupName, consumer);
        return consumer;
    }

    /**
     * 获取所有的Consumer对象
     *
     * @return
     */
    public static Map<String, Consumer> getMqHolder() {
        return MQ_HOLDER;
    }
}
