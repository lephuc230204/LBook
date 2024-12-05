package com.example.lbook.dto.rq;

import lombok.Data;

@Data
public class CommentForm {
    private long bookPostId;
    private Long parentCommentId;
    private String content;
}
