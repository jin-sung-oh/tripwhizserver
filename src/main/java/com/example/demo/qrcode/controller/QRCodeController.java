package com.example.demo.qrcode.controller;

import com.example.demo.qrcode.service.QRService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/storeowner/qrcode")
@RequiredArgsConstructor
public class QRCodeController {

    private final QRService qrService;

    @Value("${com.example.uploadBasic}")
    private String uploadBasePath;

    @Value("${com.example.upload.qrcodepath}")
    private String qrCodePath;

    @Value("${com.example.upload.storagepath}")
    private String storagePath;

    @Value("${com.example.upload.movingpath}")
    private String movingPath;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('STOREOWNER')")
    public Map<String, String> generateQRCode(@RequestParam String id, @RequestParam String type) throws Exception {
        String qrFilePath = qrService.generateQRCode(id, type);

        Map<String, String> response = new HashMap<>();
        response.put("message", "QR Code generated successfully.");
        response.put("qrCodePath", qrFilePath);

        return response;
    }

    @GetMapping("/view/{type}/{qrname}")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<Resource> viewQRCode(@PathVariable String type, @PathVariable String qrname) {
        File qrFile = getFilePath(type, qrname);

        if (!qrFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Resource resource = new FileSystemResource(qrFile);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    private File getFilePath(String type, String qrname) {
        Path folderPath = switch (type.toUpperCase()) {
            case "STORAGE" -> Paths.get(uploadBasePath, storagePath);
            case "MOVING" -> Paths.get(uploadBasePath, movingPath);
            case "ORDER" -> Paths.get(uploadBasePath, qrCodePath);
            default -> throw new IllegalArgumentException("Invalid QR code type: " + type);
        };
        return folderPath.resolve(qrname + ".png").toFile();
    }
}
