package com.example.demo.member.service;

import com.example.demo.member.domain.Member;
import com.example.demo.member.dto.MemberDTO;
import com.example.demo.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public boolean saveMember(MemberDTO memberDTO) {
        try {
            Member member = Member.builder()
                    .email(memberDTO.getEmail())
                    .name(memberDTO.getName())
                    .pw(memberDTO.getPw())
                    .delFlag(false)
                    .build();

            memberRepository.save(member);
            log.info("Member successfully saved: {}", memberDTO.getEmail());
            return true;
        } catch (Exception e) {
            log.error("Error saving member: {}", memberDTO.getEmail(), e);
            return false;
        }
    }

    public List<MemberDTO> getMemberList() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(member -> new MemberDTO(member.getEmail(), member.getPw(), member.getName()))
                .collect(Collectors.toList());
    }
}
