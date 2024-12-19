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
    private int amount;
    private MultipartFile image;
    private int length;
    private int weight;
    private int width;
    private int height;

}
