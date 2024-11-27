package com.example.demo.nationality.controller;

import com.example.demo.nationality.domain.Nationality;
import com.example.demo.nationality.repository.NationalityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nationality")
public class NationalityController {

    private final NationalityRepository nationalityRepository;

    public NationalityController(NationalityRepository nationalityRepository) {
        this.nationalityRepository = nationalityRepository;
    }

    @GetMapping
    public ResponseEntity<List<Nationality>> getAllNationalities() {
        return ResponseEntity.ok(nationalityRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nationality> getNationalityById(@PathVariable Long id) {
        return nationalityRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
