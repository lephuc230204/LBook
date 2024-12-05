package com.example.lbook.service;

import com.example.lbook.dto.rp.CommentDto;
import com.example.lbook.dto.rq.CommentForm;

import java.util.List;

public interface CommentService {
    CommentDto createComment(CommentForm commentForm);
    List<CommentDto> getAllComments();
    List<CommentDto> getCommentsByParentId(Long parentCommentId);
    String updateComment(long commentId, CommentForm commentForm);
    String deleteComment(long commentId);
    String likeComment(long commentId);

}
