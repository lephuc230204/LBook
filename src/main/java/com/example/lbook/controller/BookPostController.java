package com.example.lbook.controller;

import com.example.lbook.dto.rp.BookPostDto;
import com.example.lbook.dto.rq.BookPostForm;
import com.example.lbook.service.BookPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/book-post")
public class BookPostController {
    private final BookPostService bookPostService;

    @PostMapping("")
    public ResponseEntity<BookPostDto> create(@RequestBody BookPostForm form) {
        return ResponseEntity.ok(bookPostService.create(form));
    }

    @GetMapping("")
    public ResponseEntity<List<BookPostDto>> getAll() {
        return ResponseEntity.ok(bookPostService.getAll());
    }

    @PostMapping("/like-post/{bookId}")
    public ResponseEntity<String> liksPost(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookPostService.likesPost(bookId));
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<String> deletePost(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookPostService.deletePost(bookId));
    }
}
