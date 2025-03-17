package com.kosta.kafka.send;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EntitySchema {

    private String type;
    private List<EntityField> fields;
    private boolean optional;
    private String name;
}
