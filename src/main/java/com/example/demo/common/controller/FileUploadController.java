package com.example.demo.common.controller;

import com.example.demo.common.dto.FileUploadDTO;
import com.example.demo.common.service.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/FileUpload")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping
    public ResponseEntity<FileUploadDTO> uploadFile(@RequestBody FileUploadDTO fileUploadDTO) {
        FileUploadDTO savedFileUpload = fileUploadService.saveFileUpload(fileUploadDTO);
        return ResponseEntity.ok(savedFileUpload);
    }

    @GetMapping
    public ResponseEntity<List<FileUploadDTO>> getAllFileUploads() {
        List<FileUploadDTO> fileUploadDTOs = fileUploadService.getAllFileUploads();
        return ResponseEntity.ok(fileUploadDTOs);
    }
}
