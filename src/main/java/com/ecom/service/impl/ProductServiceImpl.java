package com.ecom.service.impl;

import com.ecom.entity.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product) ;
    }

    @Override
    public List<Product> getAllProduct() {

        return productRepository.findAll() ;
    }

    @Override
    public Boolean deleteProduct(Integer id) {
       Product product= productRepository.findById(id).orElse(null);
       if(!ObjectUtils.isEmpty(product)){
           productRepository.delete(product);
           return true;
       }
        return false;
    }

    @Override
    public Product getProductById(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        return product;
    }

    @Override
    public Product updateProduct(Product product , MultipartFile image )  {
        Product dbProduct = getProductById(product.getId());

        dbProduct.setTitle(product.getTitle());
        dbProduct.setDescription(product.getDescription());
        dbProduct.setCategory(product.getCategory());
        dbProduct.setPrice(product.getPrice());
        dbProduct.setIsActive(product.getIsActive());
        dbProduct.setStock(product.getStock());
        dbProduct.setDiscount(product.getDiscount());

        // calculate final discounted price
        Double discount = product.getPrice() * (product.getDiscount() / 100.0);
        Double discountPrice = product.getPrice() - discount;
        dbProduct.setDiscountPrice(discountPrice);

        String imageName = image.isEmpty() ? dbProduct.getImage() : image.getOriginalFilename();
        dbProduct.setImage(imageName);

        Product updatedProduct = productRepository.save(dbProduct);

        if (!ObjectUtils.isEmpty(updatedProduct) && !image.isEmpty()) {
            try {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()
                        + File.separator + "products"
                        + File.separator + image.getOriginalFilename());

                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return updatedProduct;
    }

    @Override
    public List getAllActiveProduct() {
         List<Product> products =  productRepository.findByIsActiveTrue();
        return products;
    }

}
