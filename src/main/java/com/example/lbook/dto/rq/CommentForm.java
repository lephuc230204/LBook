package com.example.lbook.dto.rq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentForm {
    private long bookPostId;
    @NotNull(message = "parentCommentId must be not null")
    private Long parentCommentId;
    @NotBlank(message = "content must be not null")
    private String content;
}
