package com.example.lbook.dto.rp;

import com.example.lbook.entity.BookPost;
import com.example.lbook.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookPostDto {
    private Long bookPostId;
    private String title;
    private String bookImage;
    private Long likes;

    public static BookPostDto toDto(BookPost bookPost) {
        return BookPostDto.builder()
                .bookPostId(bookPost.getBookPostId())
                .title(bookPost.getTitle())
                .bookImage(bookPost.getBook() != null ? bookPost.getBook().getImage() : null)
                .likes(bookPost.getLikes())
                .build();
    }
}
