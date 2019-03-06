package com.codegym.cms.service;

import com.codegym.cms.model.Category;

public interface CategoryService {

    Iterable<Category> findAll();

    Category findById(int id);

    void save(Category category);

    void remove (int id);
}
