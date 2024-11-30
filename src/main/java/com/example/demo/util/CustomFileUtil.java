package com.example.demo.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

  @Value("${com.example.uploadBasic}")
  private String uploadPath;

  @Value("${com.example.upload.productpath}")
  private String productPath;

  @Value("${com.example.upload.qrcodepath}")
  private String qrcodePath;

  @PostConstruct
  public void init() {
    // 업로드 및 QR코드 경로 초기화
    createDirectoryIfNotExists(uploadPath + File.separator + productPath);
    createDirectoryIfNotExists(uploadPath + File.separator + qrcodePath);

    log.info("Upload path initialized: {}", uploadPath);
    log.info("QR Code path initialized: {}", qrcodePath);
    log.info("Product path initialized: {}", productPath);
  }

  private void createDirectoryIfNotExists(String path) {
    File directory = new File(path);
    if (!directory.exists()) {
      directory.mkdirs();
    }
  }

  public String uploadQRCodeFile(MultipartFile file) throws IOException {
    String savedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    Path savePath = Paths.get(uploadPath + File.separator+ qrcodePath, savedName);
    Files.copy(file.getInputStream(), savePath);
    log.info("Saved QR Code file at: {}", savePath);
    return savedName;
  }


  public String uploadProductImageFile(MultipartFile file) throws IOException {
    String savedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    Path savePath = Paths.get(uploadPath + File.separator + productPath, savedName);
    Files.copy(file.getInputStream(), savePath);

    // 썸네일 생성
    createThumbnail(file, savedName);

    log.info("Saved product image file at: {}", savePath);
    return savedName;
  }


  private void createThumbnail(MultipartFile file, String savedName) throws IOException {
    String thumbnailName = "s_" + savedName;
    Path thumbnailPath = Paths.get(uploadPath + File.separator + productPath, thumbnailName);

    Thumbnails.of(file.getInputStream())
            .size(200, 200) // 썸네일 크기 설정
            .toFile(thumbnailPath.toFile());

    log.info("Created thumbnail file at: {}", thumbnailPath);
  }
}

