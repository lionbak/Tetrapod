package com.kosta.kafka.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kosta.kafka.common.LongSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "my_topic_board10")
@Builder
public class BoardEntity implements Serializable {

    @Id
    @GenericGenerator(name = "global_seq_id", strategy = "com.kosta.kafka.snowflake.SnowFlakeGenerator")
    @GeneratedValue(generator = "global_seq_id")
    @JsonSerialize(using = LongSerializer.class)
    @Column(name = "bseq")
    private Long bseq;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Column(name = "regid")
    private String regid;

    @Column(name = "testseq")
    private Long testseq;

    @Column(name = "regdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regdate;

}
