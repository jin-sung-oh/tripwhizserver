package com.example.demo.qrcode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
@Log4j2
public class QRService {

    @Value("${com.example.uploadBasic}")
    private String uploadBasePath;

    @Value("${com.example.upload.qrcodepath}")
    private String qrCodePath; // 주문 QR 코드 경로

    @Value("${com.example.upload.storagepath}")
    private String storagePath; // 보관 QR 코드 경로

    @Value("${com.example.upload.movingpath}")
    private String movingPath; // 이동 QR 코드 경로


    public String generateQRCode(Long id, String type) throws WriterException, IOException {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        // QR 텍스트를 생성 (예: ID를 문자열로 변환)
        String qrText = "ID:" + id;

        // 파일 이름을 고유하게 설정
        String fileName = id + "-" + type;

        // 기존 generateQRCode(String qrText, String type, String fileName) 메서드 호출
        return generateQRCode(qrText, type, fileName);
    }


    public String generateQRCode(String qrText, String type, String fileName) throws WriterException, IOException {
        if (qrText == null || qrText.isEmpty()) {
            throw new IllegalArgumentException("QR text cannot be null or empty");
        }

        Path folderPath = getFolderPath(type);
        BitMatrix bitMatrix = new QRCodeWriter().encode(qrText, BarcodeFormat.QR_CODE, 200, 200);

        if (!folderPath.toFile().exists()) {
            folderPath.toFile().mkdirs();
        }

        Path targetFilePath = folderPath.resolve(fileName + ".png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", targetFilePath);

        log.info("QR Code generated at: {}", targetFilePath);
        return targetFilePath.toString();
    }

    /**
     * QR 코드 저장 경로를 가져오는 메서드
     */
    private Path getFolderPath(String type) {
        return switch (type.toUpperCase()) {
            case "STORAGE" -> Paths.get(uploadBasePath, storagePath); // 보관용 QR 코드 경로
            case "MOVING" -> Paths.get(uploadBasePath, movingPath); // 이동용 QR 코드 경로
            case "ORDER" -> Paths.get(uploadBasePath, qrCodePath); // 주문용 QR 코드 경로
            default -> throw new IllegalArgumentException("Invalid QR code type: " + type); // 유효하지 않은 타입 처리
        };
    }
}
