package com.example.lbook.repository;

import com.example.lbook.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByDistrictIdAndProvinceIdAndWardIdAndNumberHouse(
            int districtId, int provinceId, String wardId, String numberHouse);
}
