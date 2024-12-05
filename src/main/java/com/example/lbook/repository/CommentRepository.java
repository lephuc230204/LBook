package com.example.lbook.repository;

import com.example.lbook.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByParentComment_CommentId(Long commentId);
}
