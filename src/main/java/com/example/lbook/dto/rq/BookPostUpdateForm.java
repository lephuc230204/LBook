package com.example.lbook.dto.rq;

import lombok.Data;
import java.time.LocalDate;
import java.util.PrimitiveIterator;

@Data
public class BookPostUpdateForm {
    private String title;
    private Long Like;
}
