package com.kosta.kafka.controller;

import com.kosta.kafka.common.Response;
import com.kosta.kafka.dto.BoardDto;
import com.kosta.kafka.service.BoardService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public Response getBoards(){
        return new Response("List Call!","List Return",boardService.getBoards());
    }

    @PostMapping("/create")
    public Response createBoard(@RequestBody BoardDto boardDto){
        return new Response("Create Call!","Create Return",boardService.createBoard(boardDto));
    }

    @DeleteMapping("/delete/{bseq}")
    public Response deleteBoard(@PathVariable("bseq") String bseq){
        System.out.println("Deleting board with bseq: " + bseq);
        boardService.deleteBoard(Long.parseLong(bseq));
        return new Response("Delete Call!","Delete Return",null);
    }
}
