package com.example.demo.member.controller;

import com.example.demo.member.dto.MemberDTO;
import com.example.demo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/member")
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/save")
    public ResponseEntity<String> saveMember(@RequestBody MemberDTO memberDTO) {
        log.info("========================================");
        log.info("Saving Member: {}", memberDTO);
        log.info("========================================");

        boolean isSaved = memberService.saveMember(memberDTO);

        if (isSaved) {
            return ResponseEntity.ok("Member successfully saved.");
        } else {
            return ResponseEntity.badRequest().body("Failed to save member.");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<List<MemberDTO>> getMemberList() {
        log.info("Fetching member list");
        List<MemberDTO> memberList = memberService.getMemberList();

        if (!memberList.isEmpty()) {
            log.info("Successfully retrieved member list.");
            return ResponseEntity.ok(memberList);
        } else {
            log.info("No members found.");
            return ResponseEntity.noContent().build();
        }
    }
}
