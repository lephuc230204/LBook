package com.example.lbook.dto.rq;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class BookForm {

    private String bookName;
    private String authorName;
    private String categoryName;
    private Double price;
    private String description;
    private Long amount;
    private MultipartFile image;
    private LocalDate postingDate;
    private boolean isApproved;

}
