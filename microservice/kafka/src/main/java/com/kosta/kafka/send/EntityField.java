package com.kosta.kafka.send;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntityField { // 데이터 저장시 필드 지정
    private String type;
    private boolean optional;
    private String field;
}
