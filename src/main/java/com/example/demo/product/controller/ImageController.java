package com.example.demo.product.controller;


import com.example.demo.product.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // 디렉토리의 이미지를 특정 Product에 저장하는 엔드포인트
    @PostMapping("/save")  // @PostMapping으로 변경
    public ResponseEntity<String> saveImages(@RequestParam Long pno) {
        try {
            String path = "C:\\zzz\\upload";
            imageService.saveImagesWithUrl(path, pno);  // 경로와 productId를 전달
            return ResponseEntity.ok("Images saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to save images: " + e.getMessage());
        }
    }
}
