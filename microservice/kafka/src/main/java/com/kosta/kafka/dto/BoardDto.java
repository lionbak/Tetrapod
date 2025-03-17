package com.kosta.kafka.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kosta.kafka.common.LongSerializer;
import com.kosta.kafka.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    @JsonSerialize(using = LongSerializer.class)
    private Long bseq;

    private String title;
    private String contents;
    private String regid;
    private Long testseq;
    private LocalDateTime regdate;

    public static BoardDto toDto(BoardEntity board) {
        return new BoardDto(
                board.getBseq(),
                board.getTitle(),
                board.getContents(),
                board.getRegid(),
                board.getTestseq(),
                board.getRegdate());
    }
}
