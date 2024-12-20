package com.example.lbook.repository;

import com.example.lbook.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher,Integer> {
    Voucher findByCode(String code);
}
