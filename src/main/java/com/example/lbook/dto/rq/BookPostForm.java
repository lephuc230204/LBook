package com.example.lbook.dto.rq;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class BookPostForm {
    private String title;
    private Long bookId;
    private MultipartFile image;
    private LocalDate postingDate;
}
