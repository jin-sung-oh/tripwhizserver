package com.example.demo.board.dto;

import com.example.demo.board.domain.BoardEntity;
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
    private String writer;
    private int viewCount;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private boolean delFlag;

    @Builder
    public BoardDTO(Long bno, String title, String bcontent, String writer, int viewCount, LocalDateTime createdDate, LocalDateTime updatedDate, boolean delFlag) {
        this.bno = bno;
        this.title = title;
        this.bcontent = bcontent;
        this.writer = writer;
        this.viewCount = viewCount;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.delFlag = delFlag;
    }

    // Entity를 DTO로 변환하는 static 메서드
    public static BoardDTO fromEntity(BoardEntity boardEntity) {
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
    public BoardEntity toEntity() {
        return BoardEntity.builder()
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
