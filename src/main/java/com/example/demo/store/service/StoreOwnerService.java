//package com.example.demo.store.service;
//
//import com.example.demo.store.domain.StoreOwner;
//import com.example.demo.store.dto.StoreOwnerDTO.StoreOwnerModifyDTO;
//import com.example.demo.store.repository.StoreOwnerRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Service;
//import java.util.List;
//
//@Service
//@Log4j2
//@RequiredArgsConstructor
//public class StoreOwnerService {
//    private final StoreOwnerRepository storeOwnerRepository;
//
//    public StoreOwner read(Long sNo) {
//        return storeOwnerRepository.findById(sNo).orElse(null);
//    }
//
//    public StoreOwner add(StoreOwner storeOwner) {
//        return storeOwnerRepository.save(storeOwner);
//    }
//
//    public void modify(Long sNo, StoreOwnerModifyDTO modifyDTO) {
//        StoreOwner storeOwner = storeOwnerRepository.findById(sNo)
//                .orElseThrow(() -> new IllegalArgumentException("StoreOwner not found"));
//        storeOwner.setSname(modifyDTO.getSname());
//        storeOwner.setEmail(modifyDTO.getEmail());
//        storeOwner.setTel(modifyDTO.getTel());
//        storeOwnerRepository.save(storeOwner);
//    }
//
//    public void softDelete(Long sNo) {
//        StoreOwner storeOwner = storeOwnerRepository.findById(sNo)
//                .orElseThrow(() -> new IllegalArgumentException("StoreOwner not found"));
//        storeOwner.setDelFlag(true);
//        storeOwnerRepository.save(storeOwner);
//    }
//
//    public List<StoreOwner> list() {
//        return storeOwnerRepository.findAll();
//    }
//}
