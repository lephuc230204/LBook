package com.example.lbook.service;

import com.example.lbook.entity.Author;

public interface AuthorService {
    Author checkOrCreateAuthor(String authorName);
}
