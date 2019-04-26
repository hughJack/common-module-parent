package cn.com.flaginfo.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/18 17:50
 */
@Component
@Slf4j
public class KafkaConsumerGroupAnnotationBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private final Set<Class<?>> nonAnnotatedClasses =
            Collections.newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>(64));
    private BeanFactory beanFactory;
    private KafkaConsumerProperties kafkaConsumerProperties;
    private boolean needInit = true;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if( this.needInit && null == this.kafkaConsumerProperties ){
            this.setKafkaConsumerProperties();
        }
        if( !this.needInit ){
            return bean;
        }
        if (!this.nonAnnotatedClasses.contains(bean.getClass())) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            Map<Method, Set<KafkaConsumerGroup>> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                    (MethodIntrospector.MetadataLookup<Set<KafkaConsumerGroup>>) method ->{
                        Set<KafkaConsumerGroup> listenerMethods = findListenerAnnotations(method);
                        return (!listenerMethods.isEmpty() ? listenerMethods : null);
                    });
            if (CollectionUtils.isEmpty(annotatedMethods)) {
                this.nonAnnotatedClasses.add(bean.getClass());
                if (log.isTraceEnabled()) {
                    log.trace("No @KafkaConsumerGroup annotations found on bean type: " + bean.getClass());
                }
            } else {
                for (Map.Entry<Method, Set<KafkaConsumerGroup>> entry : annotatedMethods.entrySet()) {
                    Method method = entry.getKey();
                    KafkaConsumerGroup kafkaConsumerGroup = entry.getValue().iterator().next();
                    if( IKafkaConsumer.class.isAssignableFrom(method.getAnnotatedReturnType().getType().getClass()) ){
                        throw new MethodReturnTypeException("the method named : " + method.getName() + " return type is not assignable from IKafkaConsumer.");
                    }
                    IKafkaConsumer kafkaConsumer = (IKafkaConsumer)ReflectionUtils.invokeMethod(method, bean);
                    KafkaMessageReceiverPool.Builder kafkaBuilder = KafkaMessageReceiverPool.KafkaMessageReceiverBuilder.builder();
                    kafkaBuilder.addIKafkaConsumer(kafkaConsumer)
                            .addMaxPollRecords(this.kafkaConsumerProperties.getMaxPollRecords())
                            .addEnableAutoCommit(this.kafkaConsumerProperties.getEnableAutoCommit())
                            .addAutoCommitIntervalMs(this.kafkaConsumerProperties.getAutoCommitIntervalMs())
                            .addSessionTimeoutMs(this.kafkaConsumerProperties.getSessionTimeoutMs())
                            .addAutoOffsetReset(this.kafkaConsumerProperties.getAutoOffsetReset())
                            .addKeyDeserializer(this.kafkaConsumerProperties.getKeyDeserializer())
                            .addValueDeserializer(this.kafkaConsumerProperties.getValueDeserializer())
                            .addTopic(kafkaConsumerGroup.topics())
                            .addGroupId(kafkaConsumerGroup.groupId())
                            .addSize(kafkaConsumerGroup.partitionNumber())
                            .build();
                }
                if (log.isDebugEnabled()) {
                    log.debug(annotatedMethods.size() + " @KafkaConsumerGroup methods processed on bean '"
                            + beanName + "' : " + annotatedMethods);
                }
            }
        }
        return bean;
    }

    private void setKafkaConsumerProperties(){
        try {
            this.kafkaConsumerProperties = this.beanFactory.getBean(KafkaConsumerProperties.class);
        }catch (BeansException e){
            if( log.isWarnEnabled() ){
                log.warn("kafka consumer properties bean is null, did not need init kafka message receiver pool");
            }
            needInit = false;
        }
    }

    /**
     * 获取方法的KafkaConsumerGroup注解
     * @param method
     * @return
     */
    private Set<KafkaConsumerGroup> findListenerAnnotations(Method method) {
        Set<KafkaConsumerGroup> listeners = new HashSet<>(1);
        KafkaConsumerGroup ann = AnnotationUtils.findAnnotation(method, KafkaConsumerGroup.class);
        if (ann != null) {
            listeners.add(ann);
        }
        return listeners;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    static class MethodReturnTypeException extends RuntimeException{

        MethodReturnTypeException(String message){
            super(message);
        }
    }
}
