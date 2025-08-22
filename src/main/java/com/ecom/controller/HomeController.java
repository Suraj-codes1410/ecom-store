package com.ecom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
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
    public String products(){
        return "product";
    }

    @GetMapping("/viewproduct")
    public String viewProduct(){
        return "viewProduct";
    }


}
