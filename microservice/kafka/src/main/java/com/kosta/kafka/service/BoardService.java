package com.kosta.kafka.service;

import com.kosta.kafka.dto.BoardDto;
import com.kosta.kafka.entity.BoardEntity;
import com.kosta.kafka.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    //게시물 조회
    @Transactional(readOnly = true)
    public List<BoardDto> getBoards(){
        List<BoardEntity> boards = boardRepository.findAll();
        List<BoardDto> boardDtos = new ArrayList<>();
        boards.forEach(s -> boardDtos.add(BoardDto.toDto(s)));
        return boardDtos;
    }

    //게시물 쓰기
    @Transactional
    public BoardDto createBoard(BoardDto boardDto){
        BoardEntity board = new BoardEntity();
        board.setTitle(boardDto.getTitle());
        board.setContents(boardDto.getContents());
        board.setRegdate(boardDto.getRegdate() != null ? boardDto.getRegdate() : LocalDateTime.now());
        board.setTestseq(boardDto.getTestseq());

        boardRepository.save(board);

        return BoardDto.toDto(board);
    }

    //게시물 삭제
    @Transactional
    public void deleteBoard(Long bseq){
        System.out.println("Deleting board with bseq: " + bseq);
        BoardEntity board = boardRepository.findById(bseq).orElseThrow(() -> {
            return new IllegalArgumentException("Board not found!");
        });
        boardRepository.deleteById(bseq);
    }


}
