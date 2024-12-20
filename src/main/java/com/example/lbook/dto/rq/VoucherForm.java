package com.example.lbook.dto.rq;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VoucherForm {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate validDate;
    private Double price;

}
