package com.kosta.kafka.cluster;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosta.kafka.dto.KafkaDto;
import com.kosta.kafka.entity.BoardEntity;
import com.kosta.kafka.send.EntityField;
import com.kosta.kafka.send.EntityPayLoad;
import com.kosta.kafka.send.EntitySchema;
import com.kosta.kafka.snowflake.SnowFlakeGenerator;
import com.kosta.kafka.websocket.controller.WebSocketController;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

@Service
@RequiredArgsConstructor
public class BoardProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final SnowFlakeGenerator snowFlakeGenerator;
    private final ObjectMapper objectMapper;
    private final WebSocketController webSocketController;


    @Getter
    private final List<Map<String, String>> dataFlow = new ArrayList<>();

    List<EntityField> fields = Arrays.asList(
            EntityField.builder().type("int64").optional(true).field("bseq").build(),
            EntityField.builder().type("string").optional(true).field("title").build(),
            EntityField.builder().type("string").optional(true).field("contents").build(),
            EntityField.builder().type("string").optional(true).field("regid").build(),
            EntityField.builder().type("int64").optional(true).field("testseq").build(),
            EntityField.builder().type("string").optional(true).field("regdate").build()
    );

    EntitySchema schema = EntitySchema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("board10")
            .build();

    public BoardEntity send(BoardEntity entity){

        String topic = "my_topic_board10";
        long bseq = snowFlakeGenerator.nextId();

        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setRegdate(LocalDateTime.now());
        EntityPayLoad payload = EntityPayLoad.builder()
                .bseq(bseq)
                .title(entity.getTitle())
                .contents(entity.getContents())
                .regid(entity.getRegid())
                .testseq(entity.getTestseq())
                .regdate(boardEntity.getRegdate().toString())
                .build();

        KafkaDto kafkaDto = KafkaDto.builder()
                .schema(schema)
                .payload(payload)
                .build();

        //JSON 포멧으로 변경
        String json = "";
        try{
            json = objectMapper.writeValueAsString(kafkaDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, json);
        webSocketController.broadcastDataFlow(json);

        dataFlow.add(Map.of(
                "source", topic,
                "target", "my_topic_board10",
                "payload", json
        ));
        log.info("kafka -> Database parsing = {}",kafkaDto);

        return entity;
    }
}
