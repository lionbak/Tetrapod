package com.kosta.kafka.cluster;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.kosta.kafka.entity.KafkaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static org.apache.kafka.common.requests.FetchMetadata.log;


@Service
@RequiredArgsConstructor
public class KafkaProducerCluster {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.template.default-topic}")
    private String topicName;

    public KafkaEntity sendMessage(String topic, KafkaEntity kafkaEntity) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try{
            json = mapper.writeValueAsString(kafkaEntity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, json);
        log.info("Kafka Producer sent from BoardEntity: " + kafkaEntity);
        return kafkaEntity;
    }

}
