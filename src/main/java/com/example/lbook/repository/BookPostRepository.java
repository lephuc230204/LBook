package com.example.lbook.repository;

import com.example.lbook.entity.BookPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookPostRepository extends JpaRepository<BookPost, Long> {

}
