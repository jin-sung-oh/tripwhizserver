package com.tripwhiz.tripwhizadminback.member.repository;

import com.tripwhiz.tripwhizadminback.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

}
