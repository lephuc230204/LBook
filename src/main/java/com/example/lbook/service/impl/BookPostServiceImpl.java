package com.example.lbook.service.impl;

import com.example.lbook.dto.rp.BookPostDto;
import com.example.lbook.dto.rq.BookPostForm;
import com.example.lbook.entity.*;
import com.example.lbook.repository.BookPostRepository;
import com.example.lbook.repository.BookRepository;
import com.example.lbook.service.AuthorService;
import com.example.lbook.service.BookPostService;
import com.example.lbook.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
// đang sửa
    @Override
    public BookPostDto update(BookPostForm bookPostForm, Long bookPostId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        BookPost bookPost = bookPostRepository.findById(bookPostId)
                .orElseThrow(() -> new UsernameNotFoundException("Post not found"));


        return null;
    }

    @Override
    public List<BookPostDto> getAll() {

        return List.of();
    }

    @Override
    public String likesPost(Long bookPostId) {

        BookPost bookPost = bookPostRepository.findById(bookPostId)
                .orElseThrow(() -> new UsernameNotFoundException("Post not found"));

        bookPost.setLikes(bookPost.getLikes() + 1);

        bookPostRepository.save(bookPost);
        return "Likes updated.";
    }

    @Override
    public String deletePost(Long bookPostId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = (User) auth.getPrincipal();

        BookPost bookPost = bookPostRepository.findById(bookPostId)
                .orElseThrow(() -> new UsernameNotFoundException("Post not found"));
        if(user.getUserId() != bookPost.getUser().getId()) {
            return "user is not authorized";
        }

        bookPostRepository.deleteById(bookPostId);

        return "Post deleted successfully";
    }

}
