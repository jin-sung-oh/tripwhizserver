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

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
@Log4j2
public class QRService {

    @Value("${com.tripwhiz.uploadBasic}")
    private String uploadBasePath;

    @Value("${com.tripwhiz.upload.qrcodepath}")
    private String qrCodePath;

    public String generateQRCode(String orderId, int totalAmount) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String qrText = "orderId=" + URLEncoder.encode(orderId, StandardCharsets.UTF_8) +
                "&totalAmount=" + URLEncoder.encode(String.valueOf(totalAmount), StandardCharsets.UTF_8);

        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 200, 200);

        Path folderPath = Paths.get(uploadBasePath, qrCodePath);
        if (!folderPath.toFile().exists()) {
            folderPath.toFile().mkdirs();
        }

        Path targetFilePath = folderPath.resolve(orderId + ".png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", targetFilePath);

        log.info("QR Code generated at: {}", targetFilePath);
        return targetFilePath.toString();
    }
}

