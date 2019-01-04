package cn.com.flaginfo.rocketmq.annotation;

import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 上午10:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MqConsumer {


    /**
     * 消费者组Id
     * 同一个消费组共享一个消费组实例
     * 默认按照消费者所在包路径创建消费者
     * @return
     */
    String group() default "";
    /**
     * 消息模式，默认为集群
     *
     * @return
     */
    MessageModel messageModel() default MessageModel.CLUSTERING;

}
