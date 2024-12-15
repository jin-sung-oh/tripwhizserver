package com.tripwhiz.tripwhizadminback.nationality.repository;

import com.tripwhiz.tripwhizadminback.nationality.entity.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NationalityRepository extends JpaRepository<Nationality, Long> {
}
