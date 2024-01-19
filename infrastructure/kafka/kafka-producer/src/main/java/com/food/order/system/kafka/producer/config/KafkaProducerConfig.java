package com.food.order.system.kafka.producer.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import com.food.order.kafka.config.data.KafkaConfigData;
import com.food.order.kafka.config.data.KafkaProducerConfigData;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig<K extends Serializable, V extends SpecificRecordBase> {

        private final KafkaConfigData kafkaConfigData;
        private final KafkaProducerConfigData kafkaProducerConfigData;

        @Bean
        public Map<String, Object> producerConfig() {
                Map<String, Object> props = new HashMap<>();
                props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                                kafkaConfigData.getBootstrapServers());
                props.put(kafkaConfigData.getSchemaRegistryUrlKey(),
                                kafkaConfigData.getSchemaRegistryUrl());
                props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                                kafkaProducerConfigData.getKeySerializerClass());
                props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                                kafkaProducerConfigData.getValueSerializerClass());
                props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerConfigData.getBatchSize()
                                * kafkaProducerConfigData.getBatchSizeBoostFactor());
                props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerConfigData.getLingerMs());
                props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,
                                kafkaProducerConfigData.getCompressionType());
                props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfigData.getAcks());
                props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,
                                kafkaProducerConfigData.getRequestTimeoutMs());
                props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerConfigData.getRetryCount());
                return props;
        }

        @Bean
        public ProducerFactory<K, V> producerFactory() {
                DefaultKafkaProducerFactory<K, V> factory =
                                new DefaultKafkaProducerFactory<>(producerConfig());
                return factory;
        }

        @Bean
        public KafkaTemplate<K, V> kafkaTemplate(ProducerFactory producerFactory) {
                return new KafkaTemplate<>(producerFactory);
        }
}
