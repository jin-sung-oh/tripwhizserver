package com.example.demo.store.controller;

import com.example.demo.store.dto.SpotDTO.SpotDTO;
import com.example.demo.store.service.SpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/spot")
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;

    // 특정 Spot 조회
    @GetMapping("/{spno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpotDTO> read(@PathVariable Long spno) {
        return ResponseEntity.ok(spotService.read(spno));
    }

    // 모든 Spot 리스트 조회
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SpotDTO>> list() {
        return ResponseEntity.ok(spotService.list());
    }

    // Spot 추가
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpotDTO> add(@RequestBody SpotDTO spotDTO) {
        log.info("----- Spot Addition Request -----");
        log.info("Received SpotDTO: {}", spotDTO);

        // Validate received DTO (optional, depending on requirements)
        if (spotDTO.getSpotname() == null || spotDTO.getSpotname().isEmpty()) {
            log.error("Spot name is missing in the SpotDTO");
        }

        log.info("Attempting to add new Spot...");
        SpotDTO createdSpot = spotService.add(spotDTO);

        log.info("Successfully added Spot");
        log.info("Created Spot Details: {}", createdSpot);
        log.info("----- End of Spot Addition -----");

        return ResponseEntity.ok(createdSpot);
    }

    // Spot 수정
    @PutMapping("/update/{spno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> modify(@PathVariable Long spno, @RequestBody SpotDTO modifyDTO) {
        spotService.modify(spno, modifyDTO);
        return ResponseEntity.ok().build();
    }

    // Spot 삭제
    @DeleteMapping("/delete/{spno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long spno) {
        spotService.delete(spno);
        return ResponseEntity.ok().build();
    }
}
