package com.kosta.kafka.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude
@Getter
@AllArgsConstructor
public class Response<T> {

    private String success;
    private String message;
    private T data;
}
