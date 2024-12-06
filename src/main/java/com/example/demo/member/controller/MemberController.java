package com.example.demo.member.controller;

import com.example.demo.member.dto.MemberDTO;
import com.example.demo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/save")
    public ResponseEntity<String> saveMember(@RequestBody MemberDTO memberDTO) {

        log.info("========================================");

        log.info("========================================");

        log.info(memberDTO);

        log.info("========================================");

        log.info("========================================");


        boolean isSaved = memberService.saveMember(memberDTO);

        if (isSaved) {
            return ResponseEntity.ok("Member successfully saved.");
        } else {
            return ResponseEntity.badRequest().body("Failed to save member.");
        }
    }

}
