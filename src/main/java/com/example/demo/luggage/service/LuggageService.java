package com.example.demo.luggage.service;

import com.example.demo.fcm.dto.FCMRequestDTO;
import com.example.demo.fcm.service.FCMService;
import com.example.demo.luggage.dto.LuggageDTO;
import com.example.demo.luggage.entity.Luggage;
import com.example.demo.luggage.entity.LuggageMoveStatus;
import com.example.demo.luggage.repository.LuggageRepository;
import com.example.demo.member.domain.Member;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.qrcode.service.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LuggageService {

    @Autowired
    private LuggageRepository luggageRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FCMService fcmService;

    @Autowired
    private QRService qrService;

    @Value("${com.example.upload.storagepath}")
    private String storagePath;

    @Value("${com.example.upload.movingpath}")
    private String movingPath;

    public void saveLuggage(LuggageDTO luggageDTO) {
        // 사용자 이메일로 Member 조회
        Member member = memberRepository.findByEmail(luggageDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Member not found with email: " + luggageDTO.getEmail()));

        // Luggage 생성
        Luggage luggage = Luggage.builder()
                .startLat(luggageDTO.getStartPoint().getLat())
                .startLng(luggageDTO.getStartPoint().getLng())
                .endLat(luggageDTO.getEndPoint().getLat())
                .endLng(luggageDTO.getEndPoint().getLng())
                .member(member)
                .status(LuggageMoveStatus.PENDING)
                .build();

        luggageRepository.save(luggage);
    }
}
