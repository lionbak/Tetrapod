package com.kosta.kafka.controller;

import com.kosta.kafka.cluster.BoardProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
public class KafkaWebController {

    private final BoardProducer boardProducer;

    // 메시지 흐름 데이터 반환 API
    @GetMapping("/data-flow")
    public List<Map<String, String>> getDataFlow() {
        return boardProducer.getDataFlow();
    }
}
