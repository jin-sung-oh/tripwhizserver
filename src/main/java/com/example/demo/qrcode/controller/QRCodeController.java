package com.example.demo.qrcode.controller;

import com.example.demo.qrcode.service.QRService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/storeowner/qrcode")
@RequiredArgsConstructor
public class QRCodeController {

    private final QRService qrService;

    @PostMapping("/approve")
    @PreAuthorize("hasRole('STOREOWNER')")
    public Map<String, String> approveOrder(@RequestParam String ono, @RequestParam int totalAmount) throws Exception {
        // QR 코드 생성
        String qrCodeUrl = qrService.generateQRCode(ono, totalAmount);

        // 알림 메시지와 QR 코드 URL 반환
        Map<String, String> response = new HashMap<>();
        response.put("message", "주문이 완료되었습니다.");
        response.put("qrCodeUrl", qrCodeUrl);

        // TODO: 사용자에게 푸시 알림 또는 추가 작업 수행
        return response;
    }
}
