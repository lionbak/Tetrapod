package com.kosta.kafka.websocket.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastDataFlow(String message) {
        String updatedMessage = message + "<br><br> Kafka Source Connector에 의해 Topic에 메시지가 전송됩니다.";
        messagingTemplate.convertAndSend("/api/kafka/data-flow", updatedMessage); // 클라이언트에게 메시지 전송
    }

}