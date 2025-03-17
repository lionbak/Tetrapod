package com.kosta.kafka.dto;

import com.kosta.kafka.send.EntityPayLoad;
import com.kosta.kafka.send.EntitySchema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class KafkaDto implements Serializable {

    private EntitySchema schema;
    private EntityPayLoad payload;

}
