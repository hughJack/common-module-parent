package cn.com.flaginfo.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/18 10:24
 */
@Slf4j
public class KafkaMessageReceiverPool {

    private final static ConcurrentHashMap<String, KafkaMessageReceiverPool> MESSAGE_RECEIVER_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    private final List<IKafkaConsumer> kafkaConsumer;
    private ExecutorService fixThreadPool;
    private Builder builder;

    public KafkaMessageReceiverPool(Builder builder) {
        this.builder = builder;
        kafkaConsumer = new ArrayList<>(builder.size);
        this.init();
        this.start();
    }

    private void init() {
        Properties properties = this.buildKafkaProperty();
        for (int i = 0; i < this.builder.size; i++) {
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
            this.builder.iKafkaConsumer.setKafkaConsumer(consumer);
            kafkaConsumer.add(this.builder.iKafkaConsumer);
        }
    }

    private void start() {
        if (CollectionUtils.isEmpty(kafkaConsumer)) {
            return;
        }
        int size = this.builder.size;
        this.fixThreadPool = new ThreadPoolExecutor(size, size,
                0L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(0),
                new DefaultThreadFactory("pool-" + this.builder.groupId + "-thread-"),
                new ThreadPoolExecutor.AbortPolicy());
        for (IKafkaConsumer iKafkaConsumer : kafkaConsumer) {
            this.fixThreadPool.execute(iKafkaConsumer);
        }
    }

    private Properties buildKafkaProperty() {
        this.builder.afterSetProperties();
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.builder.bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, this.builder.groupId);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, this.builder.enableAutoCommit);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.builder.maxPollRecords);
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, this.builder.autoCommitIntervalMs);
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, this.builder.sessionTimeoutMs);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.builder.autoOffsetReset);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, this.builder.keyDeserializer);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, this.builder.valueDeserializer);
        return properties;
    }

    public static class Builder {
        private IKafkaConsumer iKafkaConsumer;
        private int size = 1;
        private String[] topics;
        private List<String> bootstrapServers;
        private String groupId;
        private Integer maxPollRecords;
        private Boolean enableAutoCommit = true;
        private Integer autoCommitIntervalMs;
        private Integer sessionTimeoutMs;
        private String autoOffsetReset;
        private String keyDeserializer;
        private String valueDeserializer;

        public Builder addIKafkaConsumer(IKafkaConsumer iKafkaConsumer) {
            this.iKafkaConsumer = iKafkaConsumer;
            return this;
        }

        public Builder addMaxPollRecords(Integer maxPollRecords){
            this.maxPollRecords = maxPollRecords;
            return this;
        }

        public Builder addSize(int size) {
            if (size < 0) {
                throw new IllegalArgumentException("receiver size cannot less than 0");
            }
            this.size = size;
            return this;
        }

        public Builder addTopic(String[] topics) {
            this.topics = topics;
            return this;
        }

        public Builder addBootstrapServers(List<String> bootstrapServers){
            this.bootstrapServers = bootstrapServers;
            return this;
        }

        public Builder addGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder addEnableAutoCommit(boolean enableAutoCommit) {
            this.enableAutoCommit = enableAutoCommit;
            return this;
        }

        public Builder addAutoCommitIntervalMs(Integer autoCommitIntervalMs) {
            this.autoCommitIntervalMs = autoCommitIntervalMs;
            return this;
        }

        public Builder addSessionTimeoutMs(int sessionTimeoutMs) {
            this.sessionTimeoutMs = sessionTimeoutMs;
            return this;
        }

        public Builder addAutoOffsetReset(String autoOffsetReset) {
            this.autoOffsetReset = autoOffsetReset;
            return this;
        }

        public Builder addKeyDeserializer(String keyDeserializer) {
            this.keyDeserializer = keyDeserializer;
            return this;
        }

        public Builder addValueDeserializer(String valueDeserializer) {
            this.valueDeserializer = valueDeserializer;
            return this;
        }

        private void afterSetProperties() {
            if (null == bootstrapServers) {
                throw new NullPointerException("kafkaConsumerProperties cannot be null.");
            }
            if (StringUtils.isBlank(groupId)) {
                groupId = this.iKafkaConsumer.getClass().getName() + "-group";
            }
            if (null == enableAutoCommit) {
                enableAutoCommit = true;
            }
            if( !enableAutoCommit ){
                iKafkaConsumer.setAutoManualCommitOffset(!enableAutoCommit);
            }
            if (null == maxPollRecords) {
                maxPollRecords = 10;
            }
            if (null == autoCommitIntervalMs) {
                autoCommitIntervalMs = 5000;
            }
            if (null == sessionTimeoutMs) {
                sessionTimeoutMs = 10000;
            }
            if (StringUtils.isBlank(autoOffsetReset)) {
                autoOffsetReset = "latest";
            }
            if (StringUtils.isBlank(keyDeserializer)) {
                keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
            }
            if (StringUtils.isBlank(valueDeserializer)) {
                valueDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
            }
        }

        public KafkaMessageReceiverPool build() {
            this.afterSetProperties();
            String cacheKey = this.buildCacheKey();
            KafkaMessageReceiverPool kafkaMessageReceiver = MESSAGE_RECEIVER_CONCURRENT_HASH_MAP.get(cacheKey);
            if (null != kafkaMessageReceiver) {
                return kafkaMessageReceiver;
            }
            kafkaMessageReceiver = new KafkaMessageReceiverPool(this);
            kafkaMessageReceiver.start();
            MESSAGE_RECEIVER_CONCURRENT_HASH_MAP.put(cacheKey, kafkaMessageReceiver);
            return kafkaMessageReceiver;
        }


        private String buildCacheKey() {
            List<String> topicArray = Arrays.asList(topics);
            Collections.sort(topicArray);
            StringBuilder builder = new StringBuilder();
            for (String s : topicArray) {
                builder.append(s);
                builder.append("-");
            }
            builder.append(this.iKafkaConsumer.getClass().getName());
            return builder.toString();
        }
    }

    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            this("pool-" + poolNumber.getAndIncrement() + "-thread-");
        }

        DefaultThreadFactory(String namePrefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            this.namePrefix = namePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    public static class KafkaMessageReceiverBuilder {
        public static Builder builder() {
            return new Builder();
        }
    }

    private void shutDown() {
        if (null != fixThreadPool) {
            fixThreadPool.shutdown();
        }
    }

    public static void shutDownAll() {
        if (CollectionUtils.isEmpty(MESSAGE_RECEIVER_CONCURRENT_HASH_MAP)) {
            return;
        }
        for( Map.Entry<String, KafkaMessageReceiverPool> entry : MESSAGE_RECEIVER_CONCURRENT_HASH_MAP.entrySet() ){
            entry.getValue().shutDown();
        }
    }
}
