package com.kosta.kafka.controller;


import com.kosta.kafka.cluster.BoardProducer;
import com.kosta.kafka.cluster.KafkaProducerCluster;

import com.kosta.kafka.entity.BoardEntity;
import com.kosta.kafka.entity.KafkaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    private final BoardProducer boardProducer;

    //kafka sink connect
    @PostMapping("/board")
    public ResponseEntity<?> sendMessage(@RequestBody BoardEntity boardEntity) {
        boardProducer.send(boardEntity);
        return ResponseEntity.ok("Send to Board");
    }

    private final KafkaProducerCluster producerCluster;

    //kafka source connect
    @PostMapping("/produce")
    public ResponseEntity<String> sendMessage(@RequestBody KafkaEntity kafkaEntity) {
        producerCluster.sendMessage("mytopic", kafkaEntity);
        return ResponseEntity.ok("Send to Kafka");
    }
}
