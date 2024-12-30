package com.tripwhiz.tripwhizadminback.luggage.repository;

import com.tripwhiz.tripwhizadminback.luggage.entity.LuggageMove;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LuggageMoveRepository extends JpaRepository<LuggageMove, Long> {
}
