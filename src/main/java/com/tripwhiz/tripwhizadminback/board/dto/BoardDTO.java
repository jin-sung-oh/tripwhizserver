package com.tripwhiz.tripwhizadminback.board.dto;

import com.tripwhiz.tripwhizadminback.board.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BoardDTO {
    private Long bno;
    private String title;
    private String bcontent;
    private String writer = "관리자";
    private int viewCount;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
    private boolean delFlag;

    @Builder
    public BoardDTO(Long bno, String title, String bcontent, String writer, int viewCount, LocalDateTime createdDate, LocalDateTime updatedDate, boolean delFlag) {
        this.bno = bno;
        this.title = title;
        this.bcontent = bcontent;
        this.writer = writer = "관리자";
        this.viewCount = viewCount;
        this.createdDate = createdDate == null ? LocalDateTime.now() : createdDate;
        this.updatedDate = updatedDate == null ? LocalDateTime.now() : updatedDate;
        this.delFlag = delFlag;
    }

    // Entity를 DTO로 변환하는 static 메서드
    public static BoardDTO fromEntity(Board boardEntity) {
        return BoardDTO.builder()
                .bno(boardEntity.getBno())
                .title(boardEntity.getTitle())
                .bcontent(boardEntity.getBcontent())
                .writer(boardEntity.getWriter())
                .viewCount(boardEntity.getViewCount())
                .createdDate(boardEntity.getCreatedDate())
                .updatedDate(boardEntity.getUpdatedDate())
                .delFlag(boardEntity.isDelFlag())
                .build();
    }

    // DTO를 Entity로 변환하는 메서드
    public Board toEntity() {
        return Board.builder()
                .bno(this.bno)
                .title(this.title)
                .bcontent(this.bcontent)
                .writer(this.writer)
                .viewCount(this.viewCount)
                .createdDate(this.createdDate)
                .updatedDate(this.updatedDate)
                .delFlag(this.delFlag)
                .build();
    }
}
