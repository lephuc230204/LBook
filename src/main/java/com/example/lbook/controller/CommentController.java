package com.example.lbook.controller;

import com.example.lbook.dto.rp.CommentDto;
import com.example.lbook.dto.rq.CommentForm;
import com.example.lbook.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentForm commentForm) {
        return ResponseEntity.ok(commentService.createComment(commentForm));
    }
    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());  // Gọi service để lấy tất cả các bình luận
    }
    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<CommentDto>> getReplies(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.getCommentsByParentId(commentId));
    }
    @PutMapping("/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId, @RequestBody CommentForm commentForm) {
        return ResponseEntity.ok(commentService.updateComment(commentId, commentForm));
    }
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }
    @PutMapping("/{commentId}/like")
    public  ResponseEntity<String> likeComment(@PathVariable long commentId)  {
        return ResponseEntity.ok(commentService.likeComment(commentId));
    }
}
