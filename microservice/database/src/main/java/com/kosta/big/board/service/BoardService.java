package com.kosta.big.board.service;

import com.kosta.big.board.domain.BoardEntity;
import com.kosta.big.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BoardService {

    private static final AtomicLong sequence = new AtomicLong(1);
    private final BoardRepository boardRepository;

    @Transactional
    public BoardEntity svcBoardCreateOne(BoardEntity boardEntity){
        boardEntity.setTestseq(sequence.getAndIncrement());
        return boardRepository.save(boardEntity);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "boardCache")
    public List<BoardEntity> svcBoardRead(Long bseq) {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        System.out.println("ReadOnly 동작 확인 : " + isReadOnly);
        return boardRepository.findAll()
                .stream()
                .filter(board -> board.getBseq().equals(bseq))
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardEntity svcBoardUpdate(Long bseq , BoardEntity updateEntity){
        BoardEntity existingEntity = boardRepository.findById(bseq)
                .orElseThrow(() -> {
                  return new IllegalStateException("게시글을 찾을 수 없습니다.");
                });
        existingEntity.setTitle(updateEntity.getTitle());
        existingEntity.setContents(updateEntity.getContents());
        return boardRepository.save(existingEntity);
    }

    @Transactional
    @CacheEvict(cacheNames = "boardCache", key = "#bseq") // 캐시 제거
    public void svcBoardDelete(Long bseq){
        boardRepository.findById(bseq)
                .orElseThrow(() -> {return new IllegalStateException("Bseq를 찾을 수 없습니다.");
                });
        boardRepository.deleteById(bseq);
    }

    //DTO 활용 boardInsert
//    @Transactional
//    public void svcBoardCreate(List<BoardDto> boards) {
//        List<BoardEntity> boardEntity = boards.stream()
//                .map(BoardDto :: asEntity)
//                .collect(Collectors.toList());
//
//        boardRepository.saveAll(boardEntity);
//        System.out.println("게시글 삽입 작동");
//    }

    // DTO활용 boardRead
//    @Transactional(readOnly = true)
//    public List<BoardDto> svcBoardRead(String title){
//        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
//        System.out.println("ReadOnly 동작 확인 : " + isReadOnly);
//        return boardRepository.findAll()
//                .stream()
//                .filter(board -> board.getTitle().equals(title))
//                .map(BoardDto :: entityOf)
//                .collect(Collectors.toList());
//    }
}
