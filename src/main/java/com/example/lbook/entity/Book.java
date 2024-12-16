package com.example.lbook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "book")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(nullable = false)
    private String bookName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long quantity;

    private Long currentQuantity;

    private String image;

    @Column(nullable = false)
    private LocalDate postingDate;

    @Column(nullable = true)
    private boolean isApproved;

    // Fields for shipping API
    @Column(nullable = true)
    private Integer weight; // Trọng lượng của sách (gram)

    @Column(nullable = true)
    private Integer length; // Chiều dài của sách (cm)

    @Column(nullable = true)
    private Integer width; // Chiều rộng của sách (cm)

    @Column(nullable = true)
    private Integer height; // Chiều cao của sách (cm)

    private Integer cod; // Giá trị COD nếu sách cần thu hộ
}
