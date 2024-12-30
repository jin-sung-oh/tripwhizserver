package com.tripwhiz.tripwhizadminback.spot;

import com.tripwhiz.tripwhizadminback.storeowner.entity.StoreOwner;
import com.tripwhiz.tripwhizadminback.spot.entity.Spot;
import com.tripwhiz.tripwhizadminback.spot.repository.SpotRepository;
import com.tripwhiz.tripwhizadminback.storeowner.repository.StoreOwnerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

@SpringBootTest
public class SpotManagementRepositoryTest {

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private StoreOwnerRepository storeOwnerRepository;

    @Test
    public void insert100SpotsWithRandomOwners() {
        // 모든 StoreOwner 가져오기
        List<StoreOwner> storeOwners = storeOwnerRepository.findAll();

        if (storeOwners.isEmpty()) {
            throw new IllegalStateException("StoreOwner 데이터가 없습니다. 먼저 점주 데이터를 추가하세요.");
        }

        // 랜덤 객체 생성
        Random random = new Random();

        // Spot 데이터 100개 삽입
        for (int i = 1; i <= 100; i++) {
            Spot spot = new Spot();
            spot.setSpotname("Spot " + i); // 지점 이름
            spot.setAddress("Address " + i); // 지점 주소
            spot.setDelFlag(false); // 삭제 여부

            // 무작위 StoreOwner 선택
            StoreOwner randomStoreOwner = storeOwners.get(random.nextInt(storeOwners.size()));
            spot.setStoreowner(randomStoreOwner); // 랜덤 점주와 연결

            spotRepository.save(spot);
        }

        System.out.println("100개의 Spot 데이터가 무작위 StoreOwner와 연결되어 저장되었습니다.");
    }
}
