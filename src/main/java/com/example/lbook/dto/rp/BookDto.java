package com.example.lbook.dto.rp;

import com.example.lbook.entity.Book;
import com.example.lbook.entity.BookPost;
import com.example.lbook.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private Long bookId;
    private String bookName;
    private String userName;
    private String authorName;
    private String categoryName;
    private Double price;
    private String description;
    private Long amount;
    private String image;
    private LocalDate postingDate;
    private boolean isApproved;

    public static BookDto toDto(Book book) {
        if (book == null) {
            return null;
        }

        return BookDto.builder()
                .bookId(book.getBookId())
                .bookName(book.getBookName())
                .userName(book.getUser() != null ? book.getUser().getUsername() : null)
                .authorName(book.getAuthor() != null ? book.getAuthor().getAuthorName() : null)
                .categoryName(book.getCategory() != null ? book.getCategory().getCategoryName() : null)
                .price(book.getPrice())
                .description(book.getDescription())
                .amount(book.getAmount())
                .image(book.getImage())
                .postingDate(book.getPostingDate())
                .isApproved(book.isApproved())
                .build();
    }

}
