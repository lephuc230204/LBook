package com.example.lbook.service;

import com.example.lbook.dto.rp.BookDto;
import com.example.lbook.dto.rq.BookForm;
import com.example.lbook.entity.Author;
import com.example.lbook.entity.Category;

import java.util.List;

public interface BookService {
    BookDto createBook(BookForm bookForm);

    List<BookDto> getMyBooks();

    BookDto getBookById(Long bookId);

    String updateBook(BookForm bookForm, Long bookId);

    String deleteBook(Long bookId);
}
