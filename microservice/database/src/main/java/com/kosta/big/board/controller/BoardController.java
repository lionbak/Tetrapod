package com.kosta.big.board.controller;

import com.kosta.big.board.domain.BoardEntity;
import com.kosta.big.board.dto.BoardDto;
import com.kosta.big.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RequestMapping("/api/boards")
@RestController
public class BoardController {

    private final BoardService boardService;

//    @Autowired // @RequiredArgsConstructor로 자동 생성
//    public BoardController(BoardService boardService) {
//        this.boardService = boardService;
//    }

    //게시글 작성
    @PostMapping("/create")
    public ResponseEntity<String> ctlBoardInsert (@RequestBody BoardDto boardDto){
        BoardEntity boardEntity = boardDto.asEntity();
//        System.out.println("Dto -> Entity 변환작업 : " + boardEntity);
        BoardEntity createEntity = boardService.svcBoardCreateOne(boardEntity);
//        System.out.println("Created BoardEntity : " + createEntity);
        BoardDto createDto = BoardDto.entityOf(createEntity);
//        System.out.println("Created BoardDto : " + createDto);
        return ResponseEntity.ok("Board Created"+createDto);
    }

    //게시글 조회
    @GetMapping("/{bseq}")
    public ResponseEntity<List<BoardDto>> ctlBoardList (@PathVariable("bseq") Long bseq) {
        List<BoardEntity> entities = boardService.svcBoardRead(bseq);
        List<BoardDto> dto = entities.stream()
                        .map(BoardDto :: entityOf)
                        .collect(Collectors.toList());
        System.out.println(dto);

        if(!dto.isEmpty()) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{bseq}")
    public ResponseEntity<String> ctlBoardUpdate (
            @PathVariable("bseq") Long bseq,
            @RequestBody BoardDto boardDto) {
        BoardEntity boardEntity = boardDto.asEntity();
        BoardEntity updateEntity = boardService.svcBoardUpdate(bseq,boardEntity);
        System.out.println("Created BoardEntity : " + updateEntity);
        BoardDto updateDto = BoardDto.entityOf(updateEntity);
        System.out.println("Created BoardDto : " + updateDto);
        return ResponseEntity.ok("Board Updated");
    }

    @PostMapping("/delete/{bseq}")
    public ResponseEntity<String> ctlBoardDelete (@PathVariable("bseq") Long bseq) {
        boardService.svcBoardDelete(bseq);
        System.out.println("Delete board : " + bseq);
        return ResponseEntity.ok("Board Deleted");
    }
}
