package com.example.walkingmate_back.board.controller;

import com.example.walkingmate_back.board.dto.BoardCommentRequestDTO;
import com.example.walkingmate_back.board.service.BoardCommentService;
import org.springframework.web.bind.annotation.*;

/**
 *    댓글 등록, 수정, 삭제
 *
 *   @version          1.00 / 2023.07.11
 *   @author           전우진
 */

//@Controller
@RestController
@RequestMapping("/board/comments")
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    public BoardCommentController(BoardCommentService boardCommentService) {
        this.boardCommentService = boardCommentService;
    }

    // 댓글 작성
    @PostMapping("/save")
    public int saveComment(@RequestBody BoardCommentRequestDTO boardCommentRequestDTO){
        return boardCommentService.saveComment(boardCommentRequestDTO);
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public int updateComment(@PathVariable Long id, @RequestBody BoardCommentRequestDTO boardCommentRequestDTO) {
        return boardCommentService.updateComment(id, boardCommentRequestDTO);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public int deleteComment(@PathVariable Long id) {
        return boardCommentService.deleteComment(id);
    }

}