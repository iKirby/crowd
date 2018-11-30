package com.everyone.crowd.service;

import com.everyone.crowd.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category findById(Integer id);

    void update(Integer id, String name, String description);

    int insert(Category category);

    void delete(Integer id);
}
