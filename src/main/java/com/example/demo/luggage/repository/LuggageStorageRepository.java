package com.example.demo.luggage.repository;

import com.example.demo.luggage.entity.LuggageStorage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LuggageStorageRepository extends JpaRepository<LuggageStorage, Long> {
}
