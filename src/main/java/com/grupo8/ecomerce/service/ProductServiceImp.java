package com.grupo8.ecomerce.service;

import com.grupo8.ecomerce.dto.ProductDto;
import com.grupo8.ecomerce.model.Product;
import com.grupo8.ecomerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void createProduct(Product newProduct) {
        productRepository.createProduct(newProduct);
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> productList = productRepository.getAllProducts();
        return productList;
    }

    @Override
    public List<Product> getByCategory(String category) {
        List<Product> productList = productRepository.getAllProducts();
        List<Product> productByCategoryList = productList.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        return productByCategoryList;
    }

}
