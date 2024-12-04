package com.example.lbook.service;

import com.example.lbook.dto.rp.BookDto;
import com.example.lbook.dto.rq.BookForm;
import com.example.lbook.entity.Author;
import com.example.lbook.entity.Category;

public interface BookService {
    BookDto createBook(BookForm bookForm);
}
