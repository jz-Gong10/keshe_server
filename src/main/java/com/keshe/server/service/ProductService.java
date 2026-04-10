package com.keshe.server.service;

import com.keshe.server.data.po.Product;
import com.keshe.server.repository.ProductRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Resource
    private ProductRepository productRepository;

//获取所有商品列表
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

//根据商品ID获取商品
    public Product getProductById(int productId) {
        return productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
    }

//根据分类获取商品
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByProductCategory(category);
    }
}
