package com.example.lbook.service.impl;

import com.example.lbook.entity.Author;
import com.example.lbook.repository.AuthorRepository;
import com.example.lbook.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Author checkOrCreateAuthor(String authorName) {
        Author author = authorRepository.findByAuthorName(authorName).orElse(null);
        if (author == null) {
            log.info("Creating a new Author "+authorName);
            author = new Author();
            author.setAuthorName(authorName);
            authorRepository.save(author);
        }
        return author;
    }

}
