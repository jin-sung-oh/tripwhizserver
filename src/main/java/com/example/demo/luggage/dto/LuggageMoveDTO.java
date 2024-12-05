package com.example.demo.luggage.dto;

import com.example.demo.luggage.entity.LuggageMove;
import com.example.demo.luggage.entity.LuggageMoveStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LuggageMoveDTO {

    private Long lmno; // 이동 번호
    private String sourceSpotName; // 출발 지점 이름
    private String destinationSpotName; // 도착 지점 이름
    private String email; // 회원 이메일
    private String qrCodePath; // QR 코드 경로
    private LocalDateTime moveDate; // 이동 요청 날짜
    private LocalDateTime startDate; // 출발 날짜
    private LocalDateTime endDate; // 도착 날짜
    private LuggageMoveStatus status; // 이동 상태

    public static LuggageMoveDTO toDTO(LuggageMove luggageMove) {
        return LuggageMoveDTO.builder()
                .lmno(luggageMove.getLmno())
                .sourceSpotName(luggageMove.getSourceSpot().getSpotname())
                .destinationSpotName(luggageMove.getDestinationSpot().getSpotname())
                .email(luggageMove.getEmail())
                .qrCodePath(luggageMove.getQrCodePath())
                .moveDate(luggageMove.getMoveDate())
                .startDate(luggageMove.getStartDate())
                .endDate(luggageMove.getEndDate())
                .status(luggageMove.getStatus())
                .build();
    }
}
