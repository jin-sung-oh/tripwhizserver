package com.example.demo.nationality.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "nationality")
public class Nationality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nno;

    @Column(nullable = false)
    private String nname;

    @Column(nullable = false)
    private String ncode;

}
