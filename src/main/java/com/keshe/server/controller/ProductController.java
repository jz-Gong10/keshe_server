package com.keshe.server.controller;

import com.keshe.server.data.po.Product;
import com.keshe.server.data.vo.Result;
import com.keshe.server.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    private ProductService productService;

    //获取所有商品列表
    @GetMapping("/list")
    public ResponseEntity<Result> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return Result.success(products, "获取商品列表成功");
    }

    //根据商品ID获取商品
    @GetMapping("/detail/{productId}")
    public ResponseEntity<Result> getProductById(@PathVariable int productId) {
        Product product = productService.getProductById(productId);
        return Result.success(product, "获取商品详情成功");
    }

    //根据分类获取商品
    @GetMapping("/category/{category}")
    public ResponseEntity<Result> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return Result.success(products, "获取分类商品列表成功");
    }
}
