package cn.com.flaginfo.rocketmq.boot;

import cn.com.flaginfo.rocketmq.domain.ActionMappingDO;
import lombok.extern.slf4j.Slf4j;

/**
 * 消费者启动器
 * @author: Meng.Liu
 * @date: 2018/11/22 上午10:15
 */
@Slf4j
public abstract class AbstractConsumerBoot{

    /**
     * 根据包创建消费组，同一个包下共享同一个消费组实例
     * @param actionMappingDO
     * @return
     */
    public String generationConsumerGroupId(ActionMappingDO actionMappingDO){
        return actionMappingDO.getConsumerClass().getPackage().getName().replaceAll("\\.", "-");
    }

    /**
     * 启动
     */
    public abstract void start();

    /**
     * 绑定事件
     */
    public abstract void bindAction();

    public abstract void stop();

    public abstract void restart();

}
