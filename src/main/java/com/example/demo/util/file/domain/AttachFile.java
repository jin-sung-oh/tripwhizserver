package com.example.demo.util.file.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AttachFile {

    private int ord;  // 고유 ID 필드로 설정
    private String fileName;  // 파일명

}