package com.example.lbook.dto.rq;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class BookPostForm {
    private String title;
    private String bookName;
    private String author;
    private String category;
    private Long amount;
    private Double price;
    private String description;
    private MultipartFile image;
    private LocalDate postingDate;
    private boolean isApproved;
}
