package com.tripwhiz.tripwhizadminback.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

  @Value("${com.tripwhiz.uploadBasic}")
  private String uploadPath;

  @Value("${com.tripwhiz.upload.productpath}")
  private String productPath;

  @Value("${com.tripwhiz.upload.qrcodepath}")
  private String qrcodePath;

  @Value("${com.tripwhiz.upload.storagepath}")
  private String storagePath;

  @Value("${com.tripwhiz.upload.movingpath}")
  private String movingPath;

  private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB 제한

  @PostConstruct
  public void init() {
    // 업로드 및 QR코드 경로 초기화
    createDirectoryIfNotExists(Paths.get(uploadPath, productPath));
    createDirectoryIfNotExists(Paths.get(uploadPath, qrcodePath));
    createDirectoryIfNotExists(Paths.get(uploadPath, storagePath));
    createDirectoryIfNotExists(Paths.get(uploadPath, movingPath));

    log.info("Upload path initialized: {}", uploadPath);
    log.info("QR Code path initialized: {}", qrcodePath);
    log.info("Product path initialized: {}", productPath);
    log.info("Storage path initialized: {}", storagePath);
    log.info("Moving path initialized: {}", movingPath);
  }

  private void createDirectoryIfNotExists(Path path) {
    try {
      Files.createDirectories(path);
    } catch (IOException e) {
      log.error("Failed to create directory: {}", path, e);
      throw new RuntimeException("Failed to initialize upload directories", e);
    }
  }

  public String uploadQRCodeFile(MultipartFile file) throws IOException {
    validateFile(file);
    return saveFile(file, qrcodePath);
  }

  public String uploadProductImageFile(MultipartFile file) throws IOException {
    validateFile(file);
    String savedName = saveFile(file, productPath);

    // 썸네일 생성
    createThumbnail(file, savedName);

    return savedName;
  }

  public String uploadStorageFile(MultipartFile file) throws IOException {
    validateFile(file);
    return saveFile(file, storagePath);
  }

  public String uploadMovingFile(MultipartFile file) throws IOException {
    validateFile(file);
    return saveFile(file, movingPath);
  }

  private void validateFile(MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("File is empty");
    }

    if (file.getSize() > MAX_FILE_SIZE) {
      throw new IllegalArgumentException("File size exceeds the maximum limit of 10MB");
    }

    String contentType = file.getContentType();
    if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/octet-stream"))) {
      throw new IllegalArgumentException("Invalid file type: " + contentType);
    }
  }

  private String saveFile(MultipartFile file, String targetPath) throws IOException {
    String savedName = UUID.randomUUID() + "_" + sanitizeFileName(file.getOriginalFilename());
    Path savePath = Paths.get(uploadPath, targetPath, savedName);
    Files.copy(file.getInputStream(), savePath);
    log.info("Saved file at: {}", savePath);
    return savedName;
  }

  private void createThumbnail(MultipartFile file, String savedName) throws IOException {
    String thumbnailName = "s_" + savedName;
    Path thumbnailPath = Paths.get(uploadPath, productPath, thumbnailName);

    Thumbnails.of(file.getInputStream())
            .size(200, 200) // 썸네일 크기 설정
            .toFile(thumbnailPath.toFile());

    log.info("Created thumbnail file at: {}", thumbnailPath);
  }

  private String sanitizeFileName(String fileName) {
    return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
  }
}
