package com.kosta.big.board.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "board10")
public class BoardEntity implements Serializable {

    @Id
    @GenericGenerator(name="global_seq_id", strategy = "com.kosta.big.board.snowflake.SnowFlakeGenerator")
    @GeneratedValue(generator = "global_seq_id")
    @Column(name = "bseq")
    private Long bseq;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "contents", length = 500)
    private String contents;

    @Column(name = "regid", length = 50, updatable = false)
    private String regid;

    @Column(name = "testseq")
    private Long testseq;

    @Column(name = "regdate")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regdate;

    @PrePersist
    protected void onCreate() {
        if (this.regid == null) {
            this.regid = "jpauser";
        }
        if (this.regdate == null) {
            this.regdate = new Date();
        }
    }

    // 팩토리 메서드 추가
    public static BoardEntity create() {
        return new BoardEntity();
    }
}
