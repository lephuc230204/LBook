package com.example.lbook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name ="book_post")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookPost {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long bookPostId;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "bookId", nullable = false)
    private Book book;

    private Long likes =0L;

    private LocalDate postingDate;

    private String image;

    @OneToMany(mappedBy = "bookPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
}
