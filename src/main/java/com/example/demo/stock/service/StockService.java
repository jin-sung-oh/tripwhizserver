package com.example.demo.stock.service;

import com.example.demo.stock.domain.Stock;
import com.example.demo.stock.dto.StockDTO;
import com.example.demo.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    // 상품 ID로 재고를 DTO 형태로 조회
    @Transactional(readOnly = true)
    public StockDTO getStockDTO(Long pno) {
        Stock stock = stockRepository.findByProductId(pno)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품의 재고를 찾을 수 없습니다: " + pno));
        return StockDTO.fromEntity(stock);
    }

    // 재고 업데이트
    @Transactional
    public void updateStock(Long pno, int newQuantity) {
        Stock stock = stockRepository.findByProductId(pno)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품의 재고를 찾을 수 없습니다: " + pno));
        stock.updateStock(newQuantity);
        stockRepository.save(stock);
    }

    // 재고 감소
    @Transactional
    public void decreaseStock(Long pno, int quantity) {
        Stock stock = stockRepository.findByProductId(pno)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품의 재고를 찾을 수 없습니다: " + pno));
        if (stock.getQuantity() < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        stock.updateStock(stock.getQuantity() - quantity);
        stockRepository.save(stock);
    }

    // 재고 증가
    @Transactional
    public void increaseStock(Long pno, int quantity) {
        Stock stock = stockRepository.findByProductId(pno)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품의 재고를 찾을 수 없습니다: " + pno));
        stock.updateStock(stock.getQuantity() + quantity);
        stockRepository.save(stock);
    }
}
