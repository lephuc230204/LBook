package com.example.lbook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "comment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_post_id", nullable = false)  // Khoá ngoại liên kết với bảng BookPost
    private BookPost bookPost;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String authorName;  // Tên người bình luận

    private LocalDate createdDate;  // Ngày tạo bình luận
}
