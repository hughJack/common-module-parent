package cn.com.flaginfo.kafka.consumer;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/19 9:15
 */
@Getter
@Setter
@Slf4j
public abstract class AbstractKafkaConsumer implements IKafkaConsumer {

    private KafkaConsumer<String, String> kafkaConsumer;
    private boolean autoManualCommitOffset = false;
    private long commitLength = 20L;
    private long completeLength = 0;
    private Duration commitInterval = Duration.ofMillis(5000);
    private LocalDateTime lastCommitTime = LocalDateTime.now();
    private ConsumerRecord<String, String> lastUnCommitRecord;

    public AbstractKafkaConsumer(){}

    @Override
    public void run() {
       while (true){
           try{
               this.checkNeedCommit();
               Optional<ConsumerRecords<String, String>> records = Optional.of( kafkaConsumer.poll(100));
               if( records.isPresent() ){
                   ConsumerRecords<String, String> consumerRecords = records.get();
                   if(!consumerRecords.isEmpty() ){
                       for(ConsumerRecord<String, String> record : consumerRecords){
                           lastUnCommitRecord = record;
                           this.onMessage(record);
                       }
                   }
               }
           }catch (Exception e){
               log.error("", e);
           }
       }
    }

    private void checkNeedCommit(){
       if( !autoManualCommitOffset ){
            return;
       }
        if( null == lastUnCommitRecord){
            return;
        }
        boolean arrivedCommitLength = this.completeLength % this.commitLength == 0;
        LocalDateTime now = LocalDateTime.now();
        boolean arrivedCommitInterval = now.isAfter(lastCommitTime.plus(commitInterval));
        if( !arrivedCommitInterval && !arrivedCommitLength ){
            return;
        }
        lastCommitTime = now;
        long offset = lastUnCommitRecord.offset();
        int partition = lastUnCommitRecord.partition();
        String topic = lastUnCommitRecord.topic();
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(offset + 1);
        Map<TopicPartition, OffsetAndMetadata> commitMap = Collections.singletonMap(topicPartition, offsetAndMetadata);
        log.info("submit kafka consumer offset: topic-[{}], partition-[{}], offset-[{}]", topic, partition, offset);
        kafkaConsumer.commitSync(commitMap);
    }

    /**
     * 当获取消息时执行
     * @param record
     */
    @Override
    public abstract void onMessage(ConsumerRecord<String, String> record);

    /**
     * 注入kafkaConsumer
     * @param kafkaConsumer
     */
    @Override
    public void setKafkaConsumer(KafkaConsumer<String, String> kafkaConsumer){
        this.kafkaConsumer = kafkaConsumer;
    }

    /**
     * 设置自动手动提交
     * @param enable
     */
    @Override
    public void setAutoManualCommitOffset(boolean enable){
        this.autoManualCommitOffset = enable;
    }

}
