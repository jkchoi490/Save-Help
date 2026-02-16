package com.save_help.Save_Help.nationalSubsidy.config;

import com.save_help.Save_Help.nationalSubsidy.kafka.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.*;
import org.springframework.context.annotation.*;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.serializer.*;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.util.*;

@EnableKafka
@Configuration
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean public NewTopic nationalSubsidyScheduledTopic() { return new NewTopic(KafkaTopics.NATIONALSUBSIDY_SCHEDULED, 1, (short) 1); }


    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());


        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaProducerFactory<>(props);
    }


    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());


        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.save_help.Save_Help.*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(Object.class, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> cf,
            KafkaTemplate<String, Object> kafkaTemplate
    ) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        factory.setConcurrency(3);

        // 수동 커밋
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        factory.setCommonErrorHandler(errorHandler(kafkaTemplate));
        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> template) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                template,
                (record, ex) -> {
                    String topic = record.topic();
                    if (KafkaTopics.SUBSIDY_CREATED.equals(topic)) {
                        return new TopicPartition(KafkaTopics.SUBSIDY_CREATED_DLQ, record.partition());
                    }
                    if (KafkaTopics.USER_CREATED.equals(topic)) {
                        return new TopicPartition(KafkaTopics.USER_CREATED_DLQ, record.partition());
                    }
                    if (KafkaTopics.USER_ELIGIBILITY_UPDATED.equals(topic)) {
                        return new TopicPartition(KafkaTopics.USER_ELIGIBILITY_UPDATED_DLQ, record.partition());
                    }
                    return new TopicPartition(topic + ".dlq", record.partition());
                }
        );

        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 3L));
        return handler;
    }

    // (개발편의) 토픽 자동 생성
    @Bean public NewTopic t1() { return new NewTopic(KafkaTopics.SUBSIDY_CREATED, 3, (short) 1); }
    @Bean public NewTopic t2() { return new NewTopic(KafkaTopics.USER_CREATED, 3, (short) 1); }
    @Bean public NewTopic t3() { return new NewTopic(KafkaTopics.USER_ELIGIBILITY_UPDATED, 3, (short) 1); }

    @Bean public NewTopic d1() { return new NewTopic(KafkaTopics.SUBSIDY_CREATED_DLQ, 3, (short) 1); }
    @Bean public NewTopic d2() { return new NewTopic(KafkaTopics.USER_CREATED_DLQ, 3, (short) 1); }
    @Bean public NewTopic d3() { return new NewTopic(KafkaTopics.USER_ELIGIBILITY_UPDATED_DLQ, 3, (short) 1); }


    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> pf) {
        return new KafkaTemplate<>(pf);
    }



}
