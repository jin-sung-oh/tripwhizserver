package com.example.demo.util.file.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AttachFile {

    private int ord;  // 고유 ID 필드로 설정
    private String fileName;  // 파일명

}