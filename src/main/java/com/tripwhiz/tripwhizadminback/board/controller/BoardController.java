package com.tripwhiz.tripwhizadminback.board.controller;


import com.tripwhiz.tripwhizadminback.board.dto.BoardDTO;
import com.tripwhiz.tripwhizadminback.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boa")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    //게시글 생성
    @PostMapping("/add")
    public ResponseEntity<Long> createBoard(@RequestBody BoardDTO boardDTO){
        Long bno = boardService.createBoard(boardDTO);
        return ResponseEntity.ok(bno);
    }

    // 리스트조회
    @GetMapping("/list")
    public ResponseEntity<List<BoardDTO>> getBoardList(){
        List<BoardDTO> boardList = boardService.getBoardList();
        return ResponseEntity.ok(boardList);
    }

    //특정게시물 읽기
    @GetMapping("/read/{bno}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Long bno){
        BoardDTO boardDTO = boardService.getBoard(bno);
        if(boardDTO != null){
            return ResponseEntity.ok(boardDTO);
        }else {
            return ResponseEntity.notFound().build();
    }

    }

    //게시글 수정
    @PutMapping("/update/{bno}")
    public ResponseEntity<Long> updateBoard(@PathVariable Long bno, @RequestBody BoardDTO boardDTO){
        boardService.updateBoard(bno, boardDTO);
        return ResponseEntity.ok(bno);
    }

    //게시글 삭제
    @DeleteMapping("/delete/{bno}")
    public ResponseEntity<Long> deleteBoard(@PathVariable Long bno){
        boardService.deleteBoard(bno);
        return ResponseEntity.ok(bno);
    }



}
