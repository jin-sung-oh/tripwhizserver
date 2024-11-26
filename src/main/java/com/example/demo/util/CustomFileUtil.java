package com.example.demo.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

  @Value("${com.example.upload.path}")
  private String uploadPath;

  @PostConstruct
  public void init() {
    File tempFolder = new File(uploadPath);

    if (!tempFolder.exists()) {
      tempFolder.mkdir();
    }

    uploadPath = tempFolder.getAbsolutePath();

    log.info("-------------------------------------");
    log.info(uploadPath);
  }

  // 파일 업로드 메서드 추가
  public String uploadFile(MultipartFile file) {
    String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    Path savePath = Paths.get(uploadPath, savedName);

    try {
      Files.copy(file.getInputStream(), savePath);

      String contentType = file.getContentType();
      if (contentType != null && contentType.startsWith("image")) { // 이미지 여부 확인
        Path thumbnailPath = Paths.get(uploadPath, "s_" + savedName);
        Thumbnails.of(savePath.toFile())
                .size(400, 400)
                .toFile(thumbnailPath.toFile());
      }
    } catch (IOException e) {
      throw new RuntimeException("File upload failed: " + e.getMessage());
    }

    return savedName;
  }

  public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {
    if (files == null || files.size() == 0) {
      return null;
    }

    List<String> uploadNames = new ArrayList<>();

    for (MultipartFile multipartFile : files) {
      String savedName = uploadFile(multipartFile); // 기존 메서드 로직 재사용
      uploadNames.add(savedName);
    }
    return uploadNames;
  }

  public ResponseEntity<Resource> getFile(String fileName) {
    Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

    if (!resource.exists()) {
      resource = new FileSystemResource(uploadPath + File.separator + "default.jpeg");
    }

    HttpHeaders headers = new HttpHeaders();

    try {
      headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
    return ResponseEntity.ok().headers(headers).body(resource);
  }

  public void deleteFiles(List<String> fileNames) {
    if (fileNames == null || fileNames.isEmpty()) {
      return;
    }

    fileNames.forEach(fileName -> {
      String thumbnailFileName = "s_" + fileName;
      Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
      Path filePath = Paths.get(uploadPath, fileName);

      try {
        Files.deleteIfExists(filePath);
        Files.deleteIfExists(thumbnailPath);
      } catch (IOException e) {
        throw new RuntimeException("File deletion failed: " + e.getMessage());
      }
    });
  }
}
