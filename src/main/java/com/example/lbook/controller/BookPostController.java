package com.example.lbook.controller;

import com.example.lbook.dto.rp.BookPostDto;
import com.example.lbook.dto.rq.BookPostForm;
import com.example.lbook.service.BookPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/book-post")
public class BookPostController {
    private final BookPostService bookPostService;

    @PostMapping("")
    public ResponseEntity<BookPostDto> create(@RequestBody BookPostForm form) {
        return ResponseEntity.ok(bookPostService.create(form));
    }
}
