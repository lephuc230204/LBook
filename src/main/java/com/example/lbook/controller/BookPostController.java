package com.example.lbook.controller;

import com.example.lbook.dto.rp.BookPostDto;
import com.example.lbook.dto.rq.BookPostForm;
import com.example.lbook.service.BookPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/book-post")
public class BookPostController {
    private final BookPostService bookPostService;

    @PostMapping("/creat")
    public ResponseEntity<BookPostDto> create(@Valid @ModelAttribute BookPostForm form) {
        return ResponseEntity.ok(bookPostService.create(form));
    }

    @GetMapping("")
    public ResponseEntity<List<BookPostDto>> getAll() {
        return ResponseEntity.ok(bookPostService.getAll());
    }

    @PostMapping("/like-bookPost/{bookPostId}")
    public ResponseEntity<String> likePost(@PathVariable Long bookPostId) {
        return ResponseEntity.ok(bookPostService.likePost(bookPostId));
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<String> deletePost(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookPostService.deletePost(bookId));
    }
}
