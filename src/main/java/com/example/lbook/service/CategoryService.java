package com.example.lbook.service;

import com.example.lbook.entity.Category;

public interface CategoryService {
    Category checkOrCreateCategory(String categoryName);
}
