package com.everyone.crowd.service;

import com.everyone.crowd.entity.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    List<Category> findAll();

    Category findById(Integer id);

    void update(Category category);

    int insert(Category category);

    void delete(Integer id);

    Map<Integer, String> getIdNameMap();
}
