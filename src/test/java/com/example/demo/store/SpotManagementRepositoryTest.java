package com.example.demo.store;

import com.example.demo.store.domain.Spot;
import com.example.demo.store.domain.SpotManagement;
import com.example.demo.store.domain.StoreOwner;
import com.example.demo.store.domain.StoreOwnerManagement;
import com.example.demo.store.repository.SpotManagementRepository;
import com.example.demo.store.repository.SpotRepository;
import com.example.demo.store.repository.StoreOwnerRepository;
import com.example.demo.store.repository.StoreOwnerManagementRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SpotManagementRepositoryTest {

    @Autowired
    private SpotManagementRepository spotManagementRepository;

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private StoreOwnerRepository storeOwnerRepository;

    @Autowired
    private StoreOwnerManagementRepository storeOwnerManagementRepository;

    @Test
    @Transactional
    @Commit
    public void insertDummies() {
        // StoreOwner 100개 자동 생성 및 저장
        IntStream.rangeClosed(1, 100).forEach(i -> {
            StoreOwner owner = new StoreOwner();
            owner.setSname("Test Owner " + i);
            owner.setId("owner" + i);
            owner.setPw("password");
            owner.setTel("010-1234-" + String.format("%04d", i));
            owner.setEmail("test" + i + "@owner.com");
            owner.setDelFlag(false);
            storeOwnerRepository.save(owner);

            log.info("Saved StoreOwner: " + owner);
        });

        // Spot 100개 자동 생성 및 저장
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Spot spot = new Spot();
            spot.setSpotname("Test Spot " + i);
            spot.setAddress("123 Test Street " + i);
            spot.setTel("010-9876-" + String.format("%04d", i));
            spot.setDelFlag(false);
            spot.setStoreowner(storeOwnerRepository.findById((long) i).orElse(null));
            spotRepository.save(spot);

            log.info("Saved Spot: " + spot);
        });

        // SpotManagement 100개 자동 생성 및 저장
        IntStream.rangeClosed(1, 100).forEach(i -> {
            SpotManagement spotManagement = new SpotManagement();
            spotManagement.setAddress("Management Avenue " + i);
            spotManagement.setTel("010-0000-" + String.format("%04d", i));
            spotManagement.setSpot(spotRepository.findById((long) i).orElse(null));
            spotManagement.setStoreowner(storeOwnerRepository.findById((long) i).orElse(null));
            spotManagementRepository.save(spotManagement);

            log.info("Saved SpotManagement: " + spotManagement);
        });

        // StoreOwnerManagement 100개 자동 생성 및 저장
        IntStream.rangeClosed(1, 100).forEach(i -> {
            StoreOwnerManagement storeOwnerManagement = new StoreOwnerManagement();
            storeOwnerManagement.setRole("Manager " + i);
            storeOwnerManagement.setStoreowner(storeOwnerRepository.findById((long) i).orElse(null));
            storeOwnerManagementRepository.save(storeOwnerManagement);

            log.info("Saved StoreOwnerManagement: " + storeOwnerManagement);
        });
    }
}
