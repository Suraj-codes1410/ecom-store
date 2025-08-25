package com.ecom.service.impl;

import com.ecom.entity.Category;
import com.ecom.repository.CategoryRepository;
import com.ecom.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Boolean existCategory(String Name) {
        return categoryRepository.existsByName(Name) ;   // custom function created in the repository
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Boolean deleteCategory(int id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(!ObjectUtils.isEmpty(category)){
            categoryRepository.delete(category);
            return true;
        }
        return false;
    }

    @Override
    public Category getCategoryById(int id) {
       Category category = categoryRepository.findById(id).orElse(null);
       return category;
    }

    @Override
    public List<Category> getAllActiveCategory() {
        List<Category> categories = categoryRepository.findByIsActiveTrue();
        return categories;
    }
}
