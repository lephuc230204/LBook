package com.example.lbook.dto.rp;

import com.example.lbook.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class CommentDto {
    private long bookPostId;
    private long commentId;
    private Long parentCommentId;
    private long userId;
    private String content;
    private Long likes;
    private List<CommentDto> replies;

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getCommentId())
                .bookPostId(comment.getBookPost().getBookPostId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .parentCommentId(comment.getParentComment() != null
                        ? comment.getParentComment().getCommentId()
                        : null)
                .replies(comment.getReplies() != null
                        ? comment.getReplies().stream().map(CommentDto::toDto).toList()
                        : null)
                .build();
    }
}
