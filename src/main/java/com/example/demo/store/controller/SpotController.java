package com.example.demo.store.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.store.domain.Spot;
import com.example.demo.store.dto.SpotDTO.SpotListDTO;
import com.example.demo.store.dto.SpotDTO.SpotModifyDTO;
import com.example.demo.store.service.SpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/spot")
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;

    @GetMapping("/read/{spno}")
    public ResponseEntity<Spot> read(@PathVariable Long spno) {
        return ResponseEntity.ok(spotService.read(spno));
    }

    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<SpotListDTO>> list(PageRequestDTO pageRequestDTO) {
        log.info("Fetching Spot List: {}", pageRequestDTO);
        PageResponseDTO<SpotListDTO> responseDTO = spotService.list(pageRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<Spot> add(@RequestBody Spot spot) {
        return ResponseEntity.ok(spotService.add(spot));
    }

    @PutMapping("/update/{spno}")
    public ResponseEntity<Void> modify(@PathVariable Long spno, @RequestBody SpotModifyDTO modifyDTO) {
        spotService.modify(spno, modifyDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{spno}")
    public ResponseEntity<Void> softDelete(@PathVariable Long spno) {
        spotService.softDelete(spno);
        return ResponseEntity.ok().build();
    }
}
