package cn.com.flaginfo.rocketmq;

import cn.com.flaginfo.rocketmq.constants.Constants;

/**
 * @author: Meng.Liu
 * @date: 2019/4/15 下午2:15
 */
public class RocketMqBoot {

    /**
     * 获取生产者Id
     * @param groupName
     * @return
     */
    public static String getProducerId(String groupName){
        return Constants.PRODUCER_ID_PREFIX + groupName;
    }

    /**
     * 获取消费者ID
     * @param groupName
     * @return
     */
    public static String getConsumerId(String groupName){
        return Constants.CONSUMER_ID_PREFIX + groupName;
    }


}
