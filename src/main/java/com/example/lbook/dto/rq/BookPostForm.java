package com.example.lbook.dto.rq;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookPostForm {
    private String title;
    private String bookName;
    private String author;
    private String category;
    private Double price;
    private String description;
    private Long amount;
    private String image;
    private LocalDate postingDate;
    private boolean isApproved;
}
