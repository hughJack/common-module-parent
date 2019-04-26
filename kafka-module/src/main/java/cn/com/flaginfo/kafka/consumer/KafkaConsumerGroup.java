package cn.com.flaginfo.kafka.consumer;

import java.lang.annotation.*;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/18 17:41
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KafkaConsumerGroup {

    /**
     * topics
     * @return
     */
    String[] topics() default {};

    /**
     * 分区数
     * @return
     */
    int partitionNumber() default 1;

    /**
     * 组名称
     * @return
     */
    String groupId() default "";
}
