package cn.com.flaginfo.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/18 10:22
 */
public interface IKafkaConsumer extends Runnable{

    /**
     * 注入kafkaConsumer
     * @param kafkaConsumer
     */
    void setKafkaConsumer(KafkaConsumer<String, String> kafkaConsumer);

    /**
     * 设置自动手动提交
     * @param enable
     */
    void setAutoManualCommitOffset(boolean enable);

    /**
     * 当获取消息时执行
     * @param record
     */
    void onMessage(ConsumerRecord<String, String> record);
}
