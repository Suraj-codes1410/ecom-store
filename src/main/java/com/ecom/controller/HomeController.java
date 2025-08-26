package com.ecom.controller;

import org.springframework.ui.Model;
import com.ecom.entity.Category;
import com.ecom.entity.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "Login";
    }

    @GetMapping("/register")
    public String register(){
        return "Register";
    }


    @GetMapping("/product")
    public String products(Model m, @RequestParam(value="category" , defaultValue = "") String category) {
        List<Category> categories = categoryService.getAllActiveCategory();
        List<Product> products = productService.getAllActiveProduct();
        m.addAttribute("categories", categories);
        m.addAttribute("products", products);
        return "product";
    }


    @GetMapping("/viewproduct")
    public String viewProduct(){
        return "viewProduct";
    }


}
