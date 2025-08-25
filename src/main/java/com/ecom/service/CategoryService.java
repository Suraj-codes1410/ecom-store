package com.ecom.service;

import com.ecom.entity.Category;

import java.util.List;

public interface CategoryService {

    public Category saveCategory(Category category);  //declaration of methods which passes an argument of object category

    public Boolean existCategory(String Name);

    public List<Category> getAllCategory();

    public Boolean deleteCategory(int id);

    public Category getCategoryById(int id);

    public List<Category> getAllActiveCategory();



}
