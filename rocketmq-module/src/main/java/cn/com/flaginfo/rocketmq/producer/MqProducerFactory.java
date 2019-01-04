package cn.com.flaginfo.rocketmq.producer;

/**
 * 消费者工厂类
 * @author: Meng.Liu
 * @date: 2018/11/29 上午10:56
 */
public class MqProducerFactory {

    /**
     * 获取生产者
     * @param groupId
     * @return
     */
    public static AbstractMqProducer getProducer(String groupId){
        return AbstractMqProducer.getInstance(groupId);
    }

}
