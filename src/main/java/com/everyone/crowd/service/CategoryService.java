package com.everyone.crowd.service;

import com.everyone.crowd.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    void update(Integer id, String name, String description);

    int insert(Category category);

    void delete(Integer id);

    Category findById(Integer id);
}
