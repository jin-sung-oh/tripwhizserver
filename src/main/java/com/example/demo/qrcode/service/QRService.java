package com.example.demo.qrcode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Service
@Transactional
@Log4j2
public class QRService {

    @Value("${com.example.upload.qrpath}")
    private String qrImagesPath;

    // QR 코드를 생성하는 메서드로, 주문 번호와 결제 금액을 포함하여 생성된 QR 코드 이미지의 Base64 URL을 반환
    public String generateQRCode(String orderId, int totalAmount) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter(); // QR 코드 생성을 담당하는 객체를 초기화

        log.info("QR Code generation started for orderId: {}, totalAmount: {}", orderId, totalAmount);

        // QR 코드에 포함할 텍스트 정보 작성
        // 주문 번호와 총 금액을 포함한 정보를 URL 인코딩하여 생성
        String qrText = "ord=" + URLEncoder.encode(orderId, StandardCharsets.UTF_8) +
                "&totalAmount=" + URLEncoder.encode(String.valueOf(totalAmount), StandardCharsets.UTF_8);

        log.info("Encoded QR text: {}", qrText);

        // QR 코드 생성을 위해 텍스트 정보와 QR 코드 형식, 크기(200x200)를 지정하여 BitMatrix로 반환
        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 200, 200);

        log.info("BitMatrix created for QR Code");

        // 저장 폴더와 파일 경로 생성
        File folder = new File(getResourcesPath() + File.separator + qrImagesPath);

        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            log.info("QR Code folder created: {}", folder.getAbsolutePath());
            if (!created) {
                log.error("Failed to create directory: {}", folder.getAbsolutePath());
                throw new IOException("Could not create directory for QR Codes");
            }
        }

        File targetFile = new File(folder, orderId + ".png");
        log.info("Target file path for QR Code: {}", targetFile.getAbsolutePath());

        // BitMatrix 데이터를 이미지로 변환하여 파일로 저장
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", targetFile.toPath());
        log.info("QR Code written to file: {}", targetFile.getAbsolutePath());

        return targetFile.getAbsolutePath(); // QR 코드 파일 경로 반환
    }

    public String getResourcesPath() {
        // 프로젝트 루트 경로에서 "src/main/resources" 경로를 지정
        String projectDir = System.getProperty("user.dir");
        String resourcesPath = Paths.get(projectDir, "src", "main", "resources").toString();

        log.info("Resources path resolved: {}", resourcesPath);

        return resourcesPath; // 실제 경로 문자열 반환
    }
}
