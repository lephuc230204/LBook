package com.example.lbook.service;

import com.example.lbook.dto.rp.BookPostDto;
import com.example.lbook.dto.rq.BookPostForm;
import com.example.lbook.entity.Author;
import com.example.lbook.entity.Category;

public interface BookPostService {
    BookPostDto create(BookPostForm bookPostForm);

}
