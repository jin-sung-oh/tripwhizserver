package com.example.demo.store.domain;

import com.example.demo.manager.entity.StoreOwner;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spno;

    private String spotname;
    private String address;
    private String tel;

    private boolean delFlag;

    @ManyToOne
    @JoinColumn(name = "storeowner", nullable = false)
    private StoreOwner storeowner;
}
