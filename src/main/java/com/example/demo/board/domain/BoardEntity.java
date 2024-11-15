package com.example.demo.board.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "board")
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(nullable = false,length = 100)
    private String title;

    @Lob
    private String bcontent;

    private String writer = "관리자";

    @Column(nullable = false)
    @Builder.Default
    private int viewCount = 0;

    @CreatedDate // 생성 시간 자동 기록
    private LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedDate // 수정 시간 자동 기록
    private LocalDateTime updatedDate = LocalDateTime.now();

    private boolean delFlag;

    public void changeDelFlag(boolean newDelFlag) {
        this.delFlag = newDelFlag;
    }


}
