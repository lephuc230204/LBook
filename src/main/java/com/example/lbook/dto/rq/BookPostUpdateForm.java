package com.example.lbook.dto.rq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.PrimitiveIterator;

@Data
public class BookPostUpdateForm {
    @NotBlank(message = "title must be not null")
    private String title;
    @NotNull(message = "like must be not null")
    private Long Like;
}
