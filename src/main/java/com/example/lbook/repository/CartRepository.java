package com.example.lbook.repository;


import com.example.lbook.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser_UserId(Long userId);

}
