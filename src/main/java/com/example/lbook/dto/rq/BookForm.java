package com.example.lbook.dto.rq;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class BookForm {
    @NotBlank(message = "bookName must be not null")
    private String bookName;
    @NotBlank(message = "authorName must be not null")
    private String authorName;
    @NotBlank(message = "categoryName must be not null")
    private String categoryName;
    @NotNull(message = "price must be not null")
    private Double price;
    private String description;
    @NotNull(message = "amount must be not null")
    @Min(value = 1, message = "amount must be greater than 0")
    private int amount;
    @NotNull(message = "image must be not null")
    private MultipartFile image;
    @NotNull(message = "length must be not null")
    private int length;
    @NotNull(message = "weight must be not null")
    private int weight;
    @NotNull(message = "width must be not null")
    private int width;
    @NotNull(message = "height must be not null")
    private int height;
}
