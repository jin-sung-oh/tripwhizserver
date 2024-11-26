package com.example.demo.util.file.controller;

import com.example.demo.util.file.domain.AttachFile;
import com.example.demo.util.file.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/upload")
@Log4j2
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("")
    public ResponseEntity<List<AttachFile>> uploadFiles(MultipartFile[] files) {
        List<AttachFile> uploadedFiles = uploadService.uploadFiles(files);
        return ResponseEntity.ok(uploadedFiles);
    }
}




