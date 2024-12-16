package com.tripwhiz.tripwhizadminback.member.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private String email;
    private String pw;
    private String name;
    private String provider;

}
