package org.norsh.api.service;

import org.norsh.api.config.ApiConfig;
import org.norsh.config.KafkaConfig;
import org.norsh.util.Converter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Producer service for publishing messages to distributed queues (e.g., Kafka).
 * <p>
 * This service enables sending structured messages to topics, ensuring 
 * scalable communication between distributed system components.
 * </p>
 *
 * <h2>Features:</h2>
 * <ul>
 *   <li>Dynamically retrieves Kafka topic configurations.</li>
 *   <li>Ensures messages are serialized as JSON before publishing.</li>
 *   <li>Supports sending messages with custom keys.</li>
 * </ul>
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Danthur Lice
 * @see <a href="https://docs.norsh.org">Norsh Documentation</a>
 */
@Service
public class QueueProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConfig kafkaConfig;
    
    /**
     * Constructs a QueueProducerService with the necessary Kafka template.
     *
     * @param kafkaTemplate the Kafka template for sending messages.
     */
    public QueueProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConfig = ApiConfig.getInstance().getKafkaConfig();
        
        if (kafkaConfig == null || kafkaConfig.getTopic() == null || kafkaConfig.getTopic().isEmpty()) {
            throw new IllegalStateException("Kafka configuration is missing or has no topics defined.");
        }
        
        // Clears Kafka configuration from memory to enhance security
        ApiConfig.getInstance().clearKafkaConfig();
    }

    /**
     * Sends a message to the default topic.
     *
     * @param key    The key for partitioning the message.
     * @param object The object to be serialized and sent.
     */
    public void send(String key, Object object) {
        sendToTopic(kafkaConfig.getTopic(), key, object);
    }

    /**
     * Sends a message to a specified Kafka topic.
     *
     * @param topic  The Kafka topic to publish the message to.
     * @param key    The key for partitioning the message.
     * @param object The object to be serialized and sent.
     */
    public void sendToTopic(String topic, String key, Object object) {
        kafkaTemplate.send(topic, key, Converter.toJson(object));
    }

}
