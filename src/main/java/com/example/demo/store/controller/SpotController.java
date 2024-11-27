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
        log.info("----- Spot Read Request -----");
        log.info("Spot ID to read: {}", spno);

        SpotDTO spotDTO = spotService.read(spno);

        log.info("Successfully retrieved Spot: {}", spotDTO);
        log.info("----- End of Spot Read Request -----");

        return ResponseEntity.ok(spotDTO);
    }

    // 모든 Spot 리스트 조회
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SpotDTO>> list() {
        log.info("----- Spot List Request -----");

        List<SpotDTO> spotList = spotService.list();

        log.info("Successfully retrieved Spot List");
        log.info("Spot List: {}", spotList);
        log.info("----- End of Spot List Request -----");

        return ResponseEntity.ok(spotList);
    }

    // Spot 추가
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpotDTO> add(@RequestBody SpotDTO spotDTO) {
        log.info("----- Spot Addition Request -----");
        log.info("Received SpotDTO: {}", spotDTO);

        SpotDTO createdSpot = spotService.add(spotDTO);

        log.info("Successfully added Spot");
        log.info("Created Spot Details: {}", createdSpot);
        log.info("----- End of Spot Addition -----");

        return ResponseEntity.ok(createdSpot);
    }

    // Spot 수정
    @PutMapping("/update/{spno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpotDTO> modify(@PathVariable Long spno, @RequestBody SpotDTO modifyDTO) {
        log.info("----- Spot Modification Request -----");
        log.info("Spot ID to modify: {}", spno);
        log.info("Received SpotDTO for modification: {}", modifyDTO);

        // 서비스 계층에서 수정된 SpotDTO를 반환받음
        SpotDTO updatedSpot = spotService.modify(spno, modifyDTO);

        log.info("Successfully modified Spot with ID: {}", spno);
        log.info("Updated SpotDTO: {}", updatedSpot);
        log.info("----- End of Spot Modification -----");

        // 수정된 SpotDTO를 응답으로 반환
        return ResponseEntity.ok(updatedSpot);
    }

    // Spot 삭제
    @DeleteMapping("/delete/{spno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long spno) {
        log.info("----- Spot Deletion Request -----");
        log.info("Spot ID to delete: {}", spno);

        spotService.delete(spno);

        log.info("Successfully deleted Spot with ID: {}", spno);
        log.info("----- End of Spot Deletion Request -----");

        return ResponseEntity.ok().build();
    }
}
