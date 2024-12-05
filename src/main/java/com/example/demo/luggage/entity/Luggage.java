package com.example.demo.luggage.entity;

import com.example.demo.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Luggage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double startLat;
    private Double startLng;
    private Double endLat;
    private Double endLng;

    @ManyToOne
    @JoinColumn(name = "member_email", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private LuggageMoveStatus status;

    private String storageQrCodePath; // 보관 QR 코드 경로
    private String movingQrCodePath; // 이동 QR 코드 경로
}
