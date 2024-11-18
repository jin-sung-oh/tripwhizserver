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
@RequestMapping("/api/order/qrcode")
@RequiredArgsConstructor
public class QRCodeController {

    private final QRService qrService; // QR 코드 생성을 담당하는 서비스 객체

    @Value("${com.example.upload.qrpath}")
    private String qrImagePath;

    // 결제 완료 시 QR 코드를 생성하는 엔드포인트
    @PostMapping("/complete")
    public Map<String, String> completeOrder(@RequestParam String ono, @RequestParam int totalAmount) throws Exception {
        // QR 코드 생성 요청 및 결과를 반환할 데이터 맵 초기화
        String qrCodeBase64 = qrService.generateQRCode(ono, totalAmount);

        // 응답 메시지 및 QR 코드 Base64 문자열 포함
        Map<String, String> response = new HashMap<>();
        response.put("message", "주문이 완료되었습니다.");
        response.put("qrCode", qrCodeBase64);


        return response; // 생성된 QR 코드와 메시지를 JSON 형식으로 반환
    }

    @GetMapping("/view/{qrname}")
    public ResponseEntity<Resource> viewQRCode(@PathVariable String qrname) throws Exception {

        File targetFile = new File(getResourcesPath()+File.separator+qrImagePath+File.separator+ qrname+".png");

        // File을 Resource로 변환
        Resource resource = new FileSystemResource(targetFile);

        // HTTP 응답 헤더 설정
        HttpHeaders headers = new HttpHeaders(); // 파일 다운로드 설정
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE); // MIME 타입 설정

        // ResponseEntity로 Resource 반환
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    }

    public String getResourcesPath() {
        // 프로젝트 루트 경로에서 "src/main/resources" 경로를 지정
        String projectDir = System.getProperty("user.dir");
        String resourcesPath = Paths.get(projectDir, "src", "main", "resources").toString();

        return resourcesPath; // 실제 경로 문자열 반환
    }

}
