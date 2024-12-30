package com.tripwhiz.tripwhizadminback.stock.service;

import com.tripwhiz.tripwhizadminback.stock.entity.Stock;
import com.tripwhiz.tripwhizadminback.stock.dto.StockDTO;
import com.tripwhiz.tripwhizadminback.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;


        // 모든 상품 재고 조회
    public List<StockDTO> getAllStocks() {
        return stockRepository.findAll()
                .stream()
                .map(stock -> new StockDTO(
                        stock.getProduct().getPno(),
                        stock.getProduct().getPname(),
                        stock.getQuantity(),
                        stock.getProduct().getPrice()))
                .collect(Collectors.toList());
    }

    // 단일 상품 재고 조회
    public StockDTO getStockDTO(Long pno) {
        return stockRepository.findById(pno)
                .map(stock -> new StockDTO(stock.getProduct().getPno(), stock.getProduct().getPname(), stock.getQuantity(), stock.getProduct().getPrice()))
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + pno));
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
