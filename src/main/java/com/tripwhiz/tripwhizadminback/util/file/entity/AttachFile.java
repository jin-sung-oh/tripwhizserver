package com.tripwhiz.tripwhizadminback.util.file.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("file_name")
    private String fileName;  // 파일명

}