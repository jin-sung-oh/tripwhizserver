package com.example.demo.store.domain;

import com.example.demo.manager.entity.StoreOwner;
import com.example.demo.store.repository.SpotRepository;
import com.example.demo.store.repository.SpotManagementRepository;
import com.example.demo.manager.repository.StoreOwnerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SptoManagerRepositoryTest {

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private StoreOwnerRepository storeOwnerRepository;

    @Autowired
    private SpotManagementRepository spotManagementRepository;

    @Test
    void testSave100SpotsAndSpotManagements() {
        // Create 100 StoreOwners
        List<StoreOwner> storeOwners = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            StoreOwner storeOwner = new StoreOwner();
            storeOwner.setSname("StoreOwner " + i);
            storeOwner.setId("owner" + i);
            storeOwner.setPw("password" + i);
            storeOwner.setEmail("owner" + i + "@example.com");
            storeOwner.setDelFlag(false);
            storeOwner.setRole("MANAGER");
            storeOwners.add(storeOwner);
        }
        List<StoreOwner> savedStoreOwners = storeOwnerRepository.saveAll(storeOwners);
        assertThat(savedStoreOwners).hasSize(100);

        // Create 100 Spots
        List<Spot> spots = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Spot spot = new Spot();
            spot.setSpotname("Spot " + i);
            spot.setAddress("Address " + i);
            spot.setTel("123-456-" + String.format("%04d", i));
            spot.setDelFlag(false);
            spot.setStoreowner(savedStoreOwners.get(i - 1)); // Assign a StoreOwner to each Spot
            spots.add(spot);
        }
        List<Spot> savedSpots = spotRepository.saveAll(spots);
        assertThat(savedSpots).hasSize(100);

        // Create 100 SpotManagements
        List<SpotManagement> spotManagements = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            SpotManagement spotManagement = new SpotManagement();
            spotManagement.setAddress("Management Address " + i);
            spotManagement.setTel("987-654-" + String.format("%04d", i));
            spotManagement.setSpot(savedSpots.get(i - 1)); // Assign a Spot to each SpotManagement
            spotManagement.setStoreowner(savedStoreOwners.get(i - 1)); // Assign the same StoreOwner
            spotManagements.add(spotManagement);
        }
        List<SpotManagement> savedSpotManagements = spotManagementRepository.saveAll(spotManagements);
        assertThat(savedSpotManagements).hasSize(100);

        // Assertions
        assertThat(spotRepository.count()).isEqualTo(100);
        assertThat(spotManagementRepository.count()).isEqualTo(100);
        assertThat(storeOwnerRepository.count()).isEqualTo(100);
    }
}
