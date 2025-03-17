package com.kosta.big.board.dto;

import com.kosta.big.board.domain.BoardEntity;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {

    private Long bseq;
    private String title;
    private String contents;
    private String regid;
    private Long testseq;
    private Date regdate;

    //Entity -> Dto 변환
    public static BoardDto entityOf(BoardEntity boardEntity){
        BoardDto boardDto = new BoardDto();
        BeanUtils.copyProperties(boardEntity, boardDto);
        return boardDto;
    }

    //Dto -> Entity 변환
    public BoardEntity asEntity(){
        BoardEntity boardEntity = BoardEntity.create();
        BeanUtils.copyProperties(this, boardEntity);
        if (boardEntity.getRegdate() == null) {
            boardEntity.setRegdate(new Date());
        }
        return boardEntity;
    }

}
