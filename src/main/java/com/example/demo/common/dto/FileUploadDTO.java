package com.example.demo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadDTO {

    private Long uno;
    private String name;
    private Set<AttachFileDTO> attachFiles;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AttachFileDTO {
        private int ord;
        private String filename;
    }
}