package com.example.lbook.dto.rq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class BookPostForm {
    @NotBlank(message = "tittle must be not null")
    private String title;
    @NotNull(message = "bookId must be not null")
    private Long bookId;
    @NotNull(message = "image must be not null")
    private MultipartFile image;
}
