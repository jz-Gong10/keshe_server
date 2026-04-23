package com.keshe.server.controller;

import com.keshe.server.data.dto.AcceptOrderDTO;
import com.keshe.server.data.dto.BuyProductDTO;
import com.keshe.server.data.dto.SellProductDTO;
import com.keshe.server.data.po.Product;
import com.keshe.server.data.vo.Result;
import com.keshe.server.service.ProductService;
import com.keshe.server.utils.FileUploadUtils;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    //购买商品
    @PostMapping("/buy")
    public ResponseEntity<Result> buyProduct(@RequestBody BuyProductDTO dto, @RequestAttribute("userId") Long userId) {
        Product product = productService.buyProduct(dto.getProductId(), userId);
        return Result.success(product, "购买成功");
    }

    //卖家接单
    @PostMapping("/accept")
    public ResponseEntity<Result> acceptOrder(@RequestBody AcceptOrderDTO dto, @RequestAttribute("userId") Long userId) {
        Product product = productService.acceptOrder(dto.getProductId(), userId);
        return Result.success(product, "接单成功");
    }

    //发布商品
    @PostMapping("/sell")
    public ResponseEntity<Result> sellProduct(@RequestBody SellProductDTO dto, @RequestAttribute("userId") Long userId) {
        Product product = productService.sellProduct(dto.getProductName(), dto.getProductDescription(), dto.getProductPrice(), dto.getProductUrl(), dto.getProductCategory(), userId);
        return Result.success(product, "发布商品成功");
    }

    //上传图片（支持单张或多张）
    @PostMapping("/upload")
    public ResponseEntity<Result> uploadImage(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> urls = FileUploadUtils.uploadFiles(files);
            // 如果只上传了一张，返回单个URL，否则返回URL列表
            if (urls.size() == 1) {
                return Result.success(urls.getFirst(), "上传成功");
            } else {
                return Result.success(urls, "上传成功");
            }
        } catch (IOException e) {
            return Result.error(500, "上传失败: " + e.getMessage());
        }
    }

}

