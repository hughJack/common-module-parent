package cn.com.flaginfo.kafka.provider;

import cn.com.flaginfo.kafka.config.KafkaConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

import javax.annotation.PostConstruct;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/10 11:53
 */
@Component
@Slf4j
public class KafkaSender implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @PostConstruct
    private void init(){
        log.info("Kafka Sender init...");
    }

    public void send(String message){
        this.send(KafkaConstants.Default_Topic, message);
    }

    public void send(String topic, String message){
        this.send(topic,message,null, null);
    }

    public void send(String topic, String message, SuccessCallback<SendResult<String, String>> successCallback, FailureCallback failureCallback){
        if(StringUtils.isBlank(topic)){
            throw new NullPointerException("kafka topic cannot be null");
        }
        if( StringUtils.isBlank(message) ){
            throw new NullPointerException("send message cannot be null.");
        }
       ListenableFuture<SendResult<String, String>> listenableFuture = getKafkaTemplate().send(topic, message);
        if( null != successCallback && null != failureCallback ){
            listenableFuture.addCallback(successCallback, failureCallback);
        }
    }

    private KafkaTemplate<String,String> getKafkaTemplate(){
        if( null == beanFactory ){
            throw new NullPointerException("spring bean factory has not been initialized.");
        }
        KafkaTemplate kafkaTemplate = beanFactory.getBean(KafkaTemplate.class);
        if( null == kafkaTemplate ){
            throw new NullPointerException("cannot find bean with KafkaTemplate.class.");
        }
        return (KafkaTemplate<String,String>)beanFactory.getBean(KafkaTemplate.class);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
