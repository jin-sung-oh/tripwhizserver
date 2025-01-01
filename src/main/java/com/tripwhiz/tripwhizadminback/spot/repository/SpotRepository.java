package com.tripwhiz.tripwhizadminback.spot.repository;

import com.tripwhiz.tripwhizadminback.spot.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<Spot, Long> {
}
