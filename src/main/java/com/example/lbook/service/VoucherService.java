package com.example.lbook.service;

import com.example.lbook.dto.rq.VoucherForm;
import com.example.lbook.entity.Voucher;

import java.time.LocalDate;

public interface VoucherService {
    String createVoucher( VoucherForm form);
}
