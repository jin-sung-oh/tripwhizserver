package com.example.demo.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

  @Value("${com.tripwhiz.uploadBasic}")
  private String uploadPath;

  @Value("${com.tripwhiz.upload.productpath}")
  private String productPath;
  @Value("${com.tripwhiz.upload.qrcodepath}")
  private String qrcodePath;

  @PostConstruct
  public void init() {
    // 업로드 및 QR코드 경로 초기화
    createDirectoryIfNotExists(uploadPath + File.separator + productPath);
    createDirectoryIfNotExists(uploadPath + File.separator + qrcodePath);

    log.info("Upload path initialized: {}", uploadPath);
    log.info("QR Code path initialized: {}", qrcodePath);
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
}
