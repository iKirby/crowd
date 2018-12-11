package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.CategoryMapper;
import com.everyone.crowd.entity.Category;
import com.everyone.crowd.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> findAll() {
        return categoryMapper.findAll();
    }

    @Override
    public Category findById(Integer id) {
        return categoryMapper.findById(id);
    }

    @Override
    @Transactional
    public void update(Integer id, String name, String description) {
        categoryMapper.update(id, name, description);
    }

    @Override
    @Transactional
    public int insert(Category category) {
        return categoryMapper.insert(category);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        categoryMapper.delete(id);
    }

    @Override
    public Map<Integer, String> getIdNameMap() {
        Map<Integer, String> idNameMap = new HashMap<>();
        for (Category category : categoryMapper.findAll()) {
            idNameMap.put(category.getId(), category.getName());
        }
        return idNameMap;
    }
}
