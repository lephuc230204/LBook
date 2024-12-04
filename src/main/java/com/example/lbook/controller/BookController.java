package com.example.lbook.controller;


import com.example.lbook.dto.rp.BookDto;
import com.example.lbook.dto.rp.BookPostDto;
import com.example.lbook.dto.rq.BookForm;
import com.example.lbook.dto.rq.BookPostForm;
import com.example.lbook.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/book")
public class BookController {
    private final BookService bookService;

    @PostMapping("/create")
    public ResponseEntity<BookDto> create(@RequestBody BookForm form) {
        return ResponseEntity.ok(bookService.createBook(form));
    }
}
