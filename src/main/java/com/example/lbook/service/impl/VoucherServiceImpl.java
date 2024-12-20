package com.example.lbook.service.impl;

import com.example.lbook.dto.rq.VoucherForm;
import com.example.lbook.entity.Voucher;
import com.example.lbook.repository.VoucherRepository;
import com.example.lbook.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public String createVoucher(VoucherForm form) {

        Voucher voucher = new Voucher();
        voucher.setCreateDate(LocalDate.now());
        voucher.setValidDate(form.getValidDate());
        voucher.setCode();
        voucher.setPrice(form.getPrice());
        voucherRepository.save(voucher);
        return voucher.getCode();

    }
}
