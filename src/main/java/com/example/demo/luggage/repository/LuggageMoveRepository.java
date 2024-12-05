package com.example.demo.luggage.repository;

import com.example.demo.luggage.entity.LuggageMove;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LuggageMoveRepository extends JpaRepository<LuggageMove, Long> {
}
