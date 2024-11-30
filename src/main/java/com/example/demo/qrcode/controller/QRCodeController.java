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
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class QRCodeController {

    private final QRService qrService;

    @Value("${com.tripwhiz.uploadBasic}")
    private String uploadBasePath;

    @Value("${com.tripwhiz.upload.qrcodepath}")
    private String qrCodePath;

    @PostMapping("/complete")
    public Map<String, String> completeOrder(@RequestParam String ono, @RequestParam int totalAmount) throws Exception {
        String qrFilePath = qrService.generateQRCode(ono, totalAmount);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Order completed successfully.");
        response.put("qrCodePath", qrFilePath);

        return response;
    }

    @GetMapping("/view/{qrname}")
    public ResponseEntity<Resource> viewQRCode(@PathVariable String qrname) {
        File qrFile = Paths.get(uploadBasePath, qrCodePath, qrname + ".png").toFile();

        if (!qrFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Resource resource = new FileSystemResource(qrFile);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
