package com.example.lbook.service;

import com.example.lbook.dto.rp.BookPostDto;
import com.example.lbook.dto.rq.BookPostForm;

public interface BookPostService {


    BookPostDto create(BookPostForm bookPostForm);
}
