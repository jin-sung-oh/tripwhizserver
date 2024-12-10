package com.example.demo.util.scrap.controller;

import com.example.demo.store.domain.Spot;
import com.example.demo.util.scrap.service.ScrapingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/scraping")
@RequiredArgsConstructor
public class ScrapingController {

    private final ScrapingService scrapingService;

    @GetMapping("/spots")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Spot>> getSpotList() throws IOException {
        log.info("Fetching spot list via ScrapingController");

        // ScrapingService를 호출하여 데이터 가져오기
        List<Spot> spotList = scrapingService.getSpotList();

        if (spotList.isEmpty()) {
            log.warn("No spots found during scraping");
            return ResponseEntity.noContent().build(); // 데이터가 없는 경우 204 응답
        }

        log.info("Successfully fetched {} spots", spotList.size());
        return ResponseEntity.ok(spotList); // 성공적으로 데이터를 가져온 경우 200 응답
    }

}
