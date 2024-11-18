package com.example.demo.stock.controller;

import com.example.demo.stock.dto.StockDTO;
import com.example.demo.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    // 모든 상품 재고 조회
    @GetMapping
    public ResponseEntity<List<StockDTO>> getAllStocks() {
        List<StockDTO> stockList = stockService.getAllStocks();
        return ResponseEntity.ok(stockList);
    }

    // 상품 재고 조회 (단일 상품)
    @GetMapping("/{pno}")
    public ResponseEntity<StockDTO> getStockQuantity(@PathVariable Long pno) {
        StockDTO stockDTO = stockService.getStockDTO(pno);
        return ResponseEntity.ok(stockDTO);
    }

    // 상품 재고 업데이트
    @PutMapping("/{pno}")
    public ResponseEntity<Void> updateStock(@PathVariable Long pno, @RequestParam int newQuantity) {
        stockService.updateStock(pno, newQuantity);
        return ResponseEntity.ok().build();
    }

    // 상품 재고 감소
    @PostMapping("/decrease/{pno}")
    public ResponseEntity<Void> decreaseStock(@PathVariable Long pno, @RequestParam int quantity) {
        stockService.decreaseStock(pno, quantity);
        return ResponseEntity.ok().build();
    }

    // 상품 재고 증가
    @PostMapping("/increase/{pno}")
    public ResponseEntity<Void> increaseStock(@PathVariable Long pno, @RequestParam int quantity) {
        stockService.increaseStock(pno, quantity);
        return ResponseEntity.ok().build();
    }

}