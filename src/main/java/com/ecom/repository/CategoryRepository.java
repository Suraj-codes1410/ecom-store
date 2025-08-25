package com.ecom.repository;

import com.ecom.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    public Boolean existsByName(String Name);

    public List<Category> findByIsActiveTrue();


}
