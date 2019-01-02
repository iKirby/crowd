package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.CategoryMapper;
import com.everyone.crowd.entity.Category;
import com.everyone.crowd.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "category")
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Cacheable(key = "'categories'")
    public List<Category> findAll() {
        return categoryMapper.findAll();
    }

    @Override
    @Cacheable(key = "#p0")
    public Category findById(Integer id) {
        return categoryMapper.findById(id);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "'categories'"),
            @CacheEvict(key = "'categoriesIdNameMap'"),
            @CacheEvict(key = "#p0.id")
    })
    public void update(Category category) {
        categoryMapper.update(category);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "'categories'"),
            @CacheEvict(key = "'categoriesIdNameMap'")
    })
    public int insert(Category category) {
        return categoryMapper.insert(category);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "'categories'"),
            @CacheEvict(key = "'categoriesIdNameMap'"),
            @CacheEvict(key = "#p0")
    })
    public void delete(Integer id) {
        categoryMapper.delete(id);
    }

    @Override
    @Cacheable(key = "'categoriesIdNameMap'")
    public Map<Integer, String> getIdNameMap() {
        System.out.print("query database");
        Map<Integer, String> idNameMap = new HashMap<>();
        for (Category category : categoryMapper.findAll()) {
            idNameMap.put(category.getId(), category.getName());
        }
        return idNameMap;
    }
}
