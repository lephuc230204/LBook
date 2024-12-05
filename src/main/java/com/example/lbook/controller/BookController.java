package com.example.lbook.controller;


import com.example.lbook.dto.rp.BookDto;
import com.example.lbook.dto.rp.BookPostDto;
import com.example.lbook.dto.rq.BookForm;
import com.example.lbook.dto.rq.BookPostForm;
import com.example.lbook.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/book")
public class BookController {
    private final BookService bookService;

    @PostMapping("/create")
    public ResponseEntity<BookDto> create(@Valid @ModelAttribute BookForm form) {
        return ResponseEntity.ok(bookService.createBook(form));
    }

    @GetMapping("/me")
    public ResponseEntity<List<BookDto>> me() {
        return ResponseEntity.ok(bookService.getMyBooks());
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDto> getBook(@PathVariable("bookId") Long bookId) {
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<String> delete(@PathVariable("bookId") Long bookId) {
        return ResponseEntity.ok(bookService.deleteBook(bookId));
    }

    @PutMapping("/update/{bookId}")
    public ResponseEntity<String> update(@PathVariable("bookId") Long bookId, @RequestBody BookForm form) {
        return ResponseEntity.ok(bookService.updateBook(form, bookId));
    }

}
