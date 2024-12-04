package com.example.lbook.service.impl;

import com.example.lbook.dto.rp.BookPostDto;
import com.example.lbook.dto.rq.BookPostForm;
import com.example.lbook.entity.Author;
import com.example.lbook.entity.Book;
import com.example.lbook.entity.Category;
import com.example.lbook.entity.User;
import com.example.lbook.repository.BookPostRepository;
import com.example.lbook.repository.BookRepository;
import com.example.lbook.service.AuthorService;
import com.example.lbook.service.BookPostService;
import com.example.lbook.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BookPostServiceImpl implements BookPostService {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookPostRepository bookPostRepository;
    @Autowired
    private BookRepository bookRepository;

    @Override
    public BookPostDto create(BookPostForm form){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = (User) auth.getPrincipal();

        Author author = authorService.checkOrCreateAuthor(form.getAuthor());

        Category category = categoryService.checkOrCreateCategory(form.getCategory());

        Book book = Book.builder()
                .user(user)
                .bookName(form.getBookName())
                .author(author)
                .category(category)
                .price(form.getPrice())
                .amount(form.getAmount())
                .description(form.getDescription())
                .isApproved(form.isApproved())
                .build();

        bookRepository.save(book);
        return null;
    }

}
