package com.example.lbook.service.impl;

import com.example.lbook.dto.rp.BookPostDto;
import com.example.lbook.dto.rq.BookPostForm;
import com.example.lbook.entity.Book;
import com.example.lbook.entity.User;
import com.example.lbook.repository.BookPostRepository;
import com.example.lbook.service.BookPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BookPostServiceImpl implements BookPostService {

    @Autowired
    private BookPostRepository bookPostRepository;

    @Override
    public BookPostDto create(BookPostForm bookPostForm){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = (User) auth.getPrincipal();

        Book book = Book.builder()
                .bookName(bookPostForm.getBookName())
                .author()
                .category()
                .price()
                .amount()
                .description()
                .isApproved()
                .build();


        return null;
    }

}
