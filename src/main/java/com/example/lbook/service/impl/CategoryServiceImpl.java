package com.example.lbook.service.impl;

import com.example.lbook.entity.Category;
import com.example.lbook.repository.CategoryRepository;
import com.example.lbook.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category checkOrCreateCategory(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName).orElse(null);
        if (category == null) {
            log.info("Creating a new Category "+categoryName);
            category = new Category();
            category.setCategoryName(categoryName);
            categoryRepository.save(category);
        }
        return category;
    }
}
