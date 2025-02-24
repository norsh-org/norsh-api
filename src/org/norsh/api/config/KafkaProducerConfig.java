package org.norsh.api.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.norsh.config.KafkaConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

/**
 * Kafka configuration for message producers.
 * <p>
 * This class configures a Kafka producer that sends messages to a distributed messaging system, ensuring reliability and
 * optimal performance.
 * </p>
 *
 * <h2>Producer Features:</h2>
 * <ul>
 *   <li>Uses {@code acks=all} to ensure all replicas acknowledge the message before confirming.</li>
 *   <li>Serializes messages using {@link StringSerializer}.</li>
 *   <li>Applies optimizations to reduce network overhead.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * KafkaTemplate<String, String> template = kafkaTemplate();
 * template.send("topic-name", "message-key", "message-value");
 * </pre>
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Danthur Lice
 * @see <a href="https://docs.norsh.org">Norsh Documentation</a>
 */
@Configuration
public class KafkaProducerConfig {

    /**
     * Creates the Kafka producer factory with necessary configurations.
     * <p>
     * The producer is optimized for reliability and performance using:
     * <ul>
     *   <li>{@code acks=all} → Ensures messages are written to all replicas.</li>
     *   <li>{@code linger.ms=5} → Batches messages for efficiency.</li>
     *   <li>{@code bootstrap.servers} → Dynamically retrieved from {@link ApiConfig}.</li>
     * </ul>
     * </p>
     *
     * @return a configured {@link ProducerFactory} instance.
     * @throws IllegalStateException if Kafka configuration is missing.
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        KafkaConfig kafkaConfig = ApiConfig.getInstance().getKafkaConfig();

        if (kafkaConfig == null) {
            throw new IllegalStateException("Kafka configuration is missing. Ensure it is properly set in ApiConfig.");
        }

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all"); // Ensures full message durability
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 5); // Reduces network overhead by batching messages

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Provides a Kafka template for sending messages.
     * <p>
     * This template abstracts Kafka producer operations, allowing services to send messages efficiently.
     * </p>
     *
     * @return a configured {@link KafkaTemplate} instance.
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
