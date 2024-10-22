package com.example.demo.common.service;

import com.example.demo.common.dto.FileUploadDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class FileUploadService {

    private final List<FileUploadDTO> fileUploads = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public FileUploadDTO saveFileUpload(FileUploadDTO fileUploadDTO) {
        FileUploadDTO savedFileUpload = FileUploadDTO.builder()
                .uno(sequence.incrementAndGet())
                .name(fileUploadDTO.getName())
                .attachFiles(fileUploadDTO.getAttachFiles().stream()
                        .map(dto -> new FileUploadDTO.AttachFileDTO(dto.getOrd(), dto.getFilename()))
                        .collect(Collectors.toSet()))
                .build();

        fileUploads.add(savedFileUpload);
        return savedFileUpload;
    }

    public List<FileUploadDTO> getAllFileUploads() {
        return new ArrayList<>(fileUploads);
    }
}
