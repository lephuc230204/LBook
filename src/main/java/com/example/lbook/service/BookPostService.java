package com.example.lbook.service;

import com.example.lbook.dto.rp.BookPostDto;
import com.example.lbook.dto.rq.BookPostForm;
import com.example.lbook.entity.Author;
import com.example.lbook.entity.Category;

import java.util.List;

public interface BookPostService {
    BookPostDto create(BookPostForm bookPostForm);

    BookPostDto update(BookPostForm bookPostForm, Long bookPostId);

    List<BookPostDto> getAll();

    String likesPost(Long bookPostId);

    String deletePost(Long bookPostId);
}
