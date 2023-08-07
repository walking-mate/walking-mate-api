package com.example.walkingmate_back.board.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BoardCommentResponseDTO {

    private Long id; // 댓글 번호 (자동 증가)

    private Long boardId;  // 게시판 번호

    private String userId;  // 사용자 id

    private String content;  // 내용

    private Long parentId; // 댓글 아이디

    private List<BoardCommentResponseDTO> children = new ArrayList<>();

    @Builder
    public BoardCommentResponseDTO(Long id, Long boardId, String userId, String content, Long parentId, LocalDateTime regTime, LocalDateTime updateTime, List<BoardCommentResponseDTO> children) {
        this.id=id;
        this.boardId=boardId;
        this.userId=userId;
        this.content=content;
        this.parentId=parentId;
        this.children=children;
    }

    public BoardCommentResponseDTO(Long id, Long boardId, String userId, String content, Long parentId, LocalDateTime regTime, LocalDateTime updateTime) {
        this.id=id;
        this.boardId=boardId;
        this.userId=userId;
        this.content=content;
        this.parentId=parentId;
    }

}
