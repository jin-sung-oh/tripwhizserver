package com.example.demo.common.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class SampleDTO {

    private String title;

    private MultipartFile[] files;

    private List<String> uploadedFileNames;

}
