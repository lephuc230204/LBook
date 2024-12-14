package com.example.lbook.dto.rq;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemForm {
    @NotNull(message = "Quantity must not be null")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Long amount;
}
