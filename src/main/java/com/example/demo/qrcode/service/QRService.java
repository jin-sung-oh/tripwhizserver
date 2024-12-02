package com.example.demo.qrcode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    public String generateQRCode(String id, String type) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String qrText = "type=" + URLEncoder.encode(type, StandardCharsets.UTF_8) +
                "&id=" + URLEncoder.encode(id, StandardCharsets.UTF_8);

        Path folderPath = getFolderPath(type);

        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 200, 200);

        if (!folderPath.toFile().exists()) {
            folderPath.toFile().mkdirs();
        }

        String fileName = type.toUpperCase() + "-" + id + ".png";
        Path targetFilePath = folderPath.resolve(fileName);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", targetFilePath);

        log.info("QR Code generated at: {}", targetFilePath);
        return targetFilePath.toString();
    }

    private Path getFolderPath(String type) {
        return switch (type.toUpperCase()) {
            case "STORAGE" -> Paths.get(uploadBasePath, storagePath);
            case "MOVING" -> Paths.get(uploadBasePath, movingPath);
            case "ORDER" -> Paths.get(uploadBasePath, qrCodePath);
            default -> throw new IllegalArgumentException("Invalid QR code type: " + type);
        };
    }
}
