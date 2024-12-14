package com.example.lbook.repository;


import com.example.lbook.entity.Book;
import com.example.lbook.entity.Cart;
import com.example.lbook.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Page<CartItem> findByCart(Cart cart, Pageable pageable);

    List<CartItem> book(Book book);

    Optional<CartItem> findByCartAndBook_BookId(Cart cart, Long bookId);

    List<CartItem> findAllById(List<Long> ids);
}
