package com.ecom.controller;

import com.ecom.entity.Category;
import com.ecom.entity.Product;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index(){
        return "admin/index";
    }


    @GetMapping("/loadaddproduct")
    public String loadaddproduct(Model m ){
        List<Category> categories=categoryService.getAllCategory();
        m.addAttribute("categories",categories);
        return "admin/addproduct";
    }


    @GetMapping("/category")
    public String category(Model m)
    {
        m.addAttribute("categorys",categoryService.getAllCategory());
        return "admin/category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {

        String imageName= file!=null?file.getOriginalFilename():"default.jpg";
        category.setImageName(imageName);

        Boolean existcategory = categoryService.existCategory(category.getName());
        if(existcategory){
            session.setAttribute("errorMsg","Category name already exists");
        }else{
            Category saveCategory = categoryService.saveCategory(category);
            if (!ObjectUtils.isEmpty(saveCategory)) {

                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"category"+File.separator+file.getOriginalFilename());

                System.out.println(path);
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

                session.setAttribute("succMsg", "Saved Successfully");
            } else {
                session.setAttribute("errorMsg", "Not saved. Internal server error");
            }

        }
        return"redirect:/admin/category";
    }
    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id , HttpSession session ){

         Boolean deleteCategory = categoryService.deleteCategory(id);
         if(deleteCategory){
             session.setAttribute("succMsg","Category delete successful");
         }else{
             session.setAttribute("errorMsg","Something went Wrong ");
         }

        return "redirect:/admin/category";

    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id,Model m){
        m.addAttribute("category",categoryService.getCategoryById(id));

        return "admin/edit_category";
    }
    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category,@RequestParam("image") MultipartFile file,HttpSession session) throws IOException {

        Category oldcategory = categoryService.getCategoryById(category.getId());
        String imageName = file.isEmpty() ? oldcategory.getImageName() : file.getOriginalFilename();


        if (!ObjectUtils.isEmpty(category)) {
            oldcategory.setName(category.getName());
            oldcategory.setIsActive(category.getIsActive());
            oldcategory.setImageName(imageName);
        }
        Category updateCategory = categoryService.saveCategory(oldcategory);

        if (!ObjectUtils.isEmpty(updateCategory)) {

            if (!file.isEmpty()) {

                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category" + File.separator + file.getOriginalFilename());

               // System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                session.setAttribute("succMsg", "Update Successful");
            }
        }else {
                session.setAttribute("errorMsg", "Something Went Wrong ");
            }
            return "redirect:/admin/loadEditCategory/" + category.getId();

        }
    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product,
                              HttpSession session,
                              @RequestParam("file") MultipartFile image) throws IOException {

        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
        product.setImage(imageName);

        Product savedProduct = productService.saveProduct(product);

        if (!ObjectUtils.isEmpty(savedProduct)) {
            if (!image.isEmpty()) {
                File saveFile  = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()
                        + File.separator + "products"
                        + File.separator + imageName);

                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg","Product Saved Successfully");
        } else {
            session.setAttribute("errorMsg","Something went Wrong");
        }

        return "redirect:/admin/loadaddproduct";
    }

    @GetMapping("/products")
    public String loadViewProduct(Model m ){
        m.addAttribute("products",productService.getAllProduct());

        return "admin/products";
    }

    @GetMapping("/deleteProduct/{id}")
            public String deleteProduct(@PathVariable int id,HttpSession session ){
            Boolean deleteProduct = productService.deleteProduct(id);

            if(deleteProduct){
   session.setAttribute("succMsg","Product Deleted Successfully");
            }else{
session.setAttribute("errorMsg","Internal server issues ");
            }
        return "redirect:/admin/products";

}
    @GetMapping("/editproducts/{id}")
    public String editProduct(@PathVariable int id, Model m ){
        m.addAttribute("product",productService.getProductById(id));
        m.addAttribute("categories",categoryService.getAllCategory());
        return "admin/editProduct";
    }
    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile image,HttpSession session, Model m ){

        Product updateProduct = productService.updateProduct(product,image);
        if(!ObjectUtils.isEmpty(updateProduct)){
            session.setAttribute("succMsg","Product Updation was Successfull");
        }else {
            session.setAttribute("errorMsg","Something went wrong ");
        }
        return "redirect:/admin/editproducts/"+product.getId();
    }

}