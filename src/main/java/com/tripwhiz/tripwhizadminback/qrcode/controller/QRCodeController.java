package com.tripwhiz.tripwhizadminback.qrcode.controller;

import com.tripwhiz.tripwhizadminback.qrcode.service.QRService;
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

    @Value("${com.tripwhiz.uploadBasic}")
    private String uploadBasePath;

    @Value("${com.tripwhiz.upload.qrcodepath}")
    private String qrCodePath;

    @Value("${com.tripwhiz.upload.storagepath}")
    private String storagePath;

    @Value("${com.tripwhiz.upload.movingpath}")
    private String movingPath;

    /**
     * QR 코드 생성 API
     */
    @PostMapping("/generate")
    @PreAuthorize("hasRole('STOREOWNER')") // "STOREOWNER" 역할을 가진 사용자만 접근 가능
    public Map<String, Map<String, String>> generateMultipleQRCodes(
            @RequestParam Long sno,
            @RequestParam Long lmno,
            @RequestParam Long lsno,
            @RequestParam String type) throws Exception {

        // 각각의 QR 코드 생성
        String snoQRPath = qrService.generateQRCode(sno, type + "-SNO");
        String lmnoQRPath = qrService.generateQRCode(lmno, type + "-LMNO");
        String lsnoQRPath = qrService.generateQRCode(lsno, type + "-LSNO");

        // 응답 메시지 구성
        Map<String, Map<String, String>> response = new HashMap<>();

        response.put("sno", Map.of("message", "QR Code for sno generated successfully.", "qrCodePath", snoQRPath));
        response.put("lmno", Map.of("message", "QR Code for lmno generated successfully.", "qrCodePath", lmnoQRPath));
        response.put("lsno", Map.of("message", "QR Code for lsno generated successfully.", "qrCodePath", lsnoQRPath));

        return response;
    }

    /**
     * QR 코드 보기 API
     */
    @GetMapping("/view/{type}/{qrname}")
    @PreAuthorize("hasRole('STOREOWNER')") // "STOREOWNER" 역할을 가진 사용자만 접근 가능
    public ResponseEntity<Resource> viewQRCode(@PathVariable String type, @PathVariable String qrname) {
        // 요청된 QR 코드 파일 경로 가져오기
        File qrFile = getFilePath(type, qrname);

        // 파일이 존재하지 않을 경우 404 응답 반환
        if (!qrFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // 파일을 리소스로 변환하여 응답에 포함
        Resource resource = new FileSystemResource(qrFile);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG); // 응답 콘텐츠 타입을 PNG로 설정

        return new ResponseEntity<>(resource, headers, HttpStatus.OK); // 파일 응답 반환
    }

    /**
     * 파일 경로를 가져오는 메서드
     */
    private File getFilePath(String type, String qrname) {
        // 타입에 따라 저장 경로를 결정
        Path folderPath = switch (type.toUpperCase()) {
            case "STORAGE" -> Paths.get(uploadBasePath, storagePath); // 보관용 QR 코드 경로
            case "MOVING" -> Paths.get(uploadBasePath, movingPath); // 이동용 QR 코드 경로
            case "ORDER" -> Paths.get(uploadBasePath, qrCodePath); // 주문용 QR 코드 경로
            default -> throw new IllegalArgumentException("Invalid QR code type: " + type); // 유효하지 않은 타입 예외 처리
        };
        // 파일 경로를 반환
        return folderPath.resolve(qrname + ".png").toFile();
    }
}
