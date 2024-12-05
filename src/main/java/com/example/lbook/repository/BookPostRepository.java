package com.example.lbook.repository;

import com.example.lbook.dto.rp.BookPostDto;
import com.example.lbook.entity.BookPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookPostRepository extends JpaRepository<BookPost, Long> {
    Optional<BookPost> findBookPostById(Long id);
}
