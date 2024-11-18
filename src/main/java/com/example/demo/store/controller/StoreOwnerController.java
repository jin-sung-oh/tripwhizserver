//package com.example.demo.store.controller;
//
//
//import com.example.demo.store.domain.StoreOwner;
//import com.example.demo.store.dto.StoreOwnerDTO.StoreOwnerModifyDTO;
//import com.example.demo.store.service.StoreOwnerService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Log4j2
//@RestController
//@RequestMapping("/api/store")
//@RequiredArgsConstructor
//public class StoreOwnerController {
//
//    private final StoreOwnerService storeOwnerService;
//
//    @GetMapping("/list")
//    public ResponseEntity<List<StoreOwner>> list() {
//        return ResponseEntity.ok(storeOwnerService.list());
//    }
//
//    @GetMapping("/read/{sno}")
//    public ResponseEntity<StoreOwner> read(@PathVariable Long sno) {
//        return ResponseEntity.ok(storeOwnerService.read(sno));
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<StoreOwner> add(@RequestBody StoreOwner storeowner) {
//        return ResponseEntity.ok(storeOwnerService.add(storeowner));
//    }
//
//    @PutMapping("/update/{sno}")
//    public ResponseEntity<Void> modify(@PathVariable Long sno, @RequestBody StoreOwnerModifyDTO modifyDTO) {
//        storeOwnerService.modify(sno, modifyDTO);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/delete/{sno}")
//    public ResponseEntity<Void> softDelete(@PathVariable Long sno) {
//        storeOwnerService.softDelete(sno);
//        return ResponseEntity.ok().build();
//    }
//}
