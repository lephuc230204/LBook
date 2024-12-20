package com.example.lbook.controller;

import com.example.lbook.dto.rq.VoucherForm;
import com.example.lbook.entity.Voucher;
import com.example.lbook.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/voucher")
public class VoucherController {

    private final VoucherService voucherService;

    @PostMapping("")
    public String createVoucher(@RequestBody VoucherForm form) {
        return voucherService.createVoucher(form);
    }
}
