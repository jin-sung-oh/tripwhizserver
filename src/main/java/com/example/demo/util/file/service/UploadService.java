package com.example.demo.util.file.service;

import com.example.demo.util.file.domain.AttachFile;
import com.example.demo.util.file.exception.UploadException;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class UploadService {

    @Value("${com.example.upload.path}")
    private String uploadFolder;

    public List<AttachFile> uploadFiles(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return new ArrayList<>();
        }

        List<AttachFile> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            try {
                // 파일 저장
                File savedFile = new File(uploadFolder, fileName);
                FileCopyUtils.copy(file.getBytes(), savedFile);

                // AttachFile 생성
                AttachFile attachFile = new AttachFile(0, fileName);
                uploadedFiles.add(attachFile);

                // 썸네일 생성 (이미지 파일인 경우)
                if (file.getContentType() != null && file.getContentType().startsWith("image")) {
                    String thumbnailFileName = "s_" + fileName;

                    @Cleanup
                    InputStream inputStream = new FileInputStream(savedFile);
                    @Cleanup
                    OutputStream outputStream = new FileOutputStream(new File(uploadFolder, thumbnailFileName));

                    Thumbnailator.createThumbnail(inputStream, outputStream, 200, 200);

                    // 썸네일도 AttachFile로 추가할 경우
                    AttachFile thumbnailFile = new AttachFile(0, thumbnailFileName);
                    uploadedFiles.add(thumbnailFile);
                }

            } catch (IOException e) {
                log.error("File upload failed: {}", e.getMessage());
                throw new UploadException("Failed to upload file: " + e.getMessage());
            }
        }

        return uploadedFiles;
    }

    public void deleteFile(String fileName) {
        try {
            File fileToDelete = new File(uploadFolder, fileName);

            if (fileToDelete.exists()) {
                boolean deleted = fileToDelete.delete();
                if (!deleted) {
                    throw new IOException("Failed to delete file: " + fileName);
                }
            }
        } catch (IOException e) {
            log.error("File deletion failed: {}", e.getMessage());
            throw new UploadException("Failed to delete file: " + fileName);
        }
    }
}

