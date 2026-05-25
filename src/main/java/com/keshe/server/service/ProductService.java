package com.keshe.server.service;

import com.keshe.server.data.po.Product;
import com.keshe.server.data.vo.Result;
import com.keshe.server.repository.ProductRepository;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
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

    //购买商品
    public Product buyProduct(int productId, Long buyerId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        // 将buyerStatus从0改为1
        product.setBuyerStatus(1);
        // 设置购买者ID
        product.setBuyerId(buyerId);

        // 保存更新
        return productRepository.save(product);
    }

    //卖家接单
    public Product acceptOrder(int productId, Long sellerId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        // 验证是否是商品的卖家
        if (!sellerId.equals(product.getSellerId())) {
            throw new RuntimeException("只有商品卖家才能接单");
        }

        // 将sellerStatus从0改为1
        product.setSellerStatus(1);

        // 保存更新
        return productRepository.save(product);
    }

    //发布商品
    public Product sellProduct(String productName, String productDescription, double productPrice, List<String> productUrl, String productCategory, Long sellerId) {
        Product product = new Product();
        product.setProductName(productName);
        product.setProductDescription(productDescription);
        product.setProductPrice(productPrice);
        product.setProductUrl(productUrl);
        product.setProductCategory(productCategory);
        product.setSellerId(sellerId);
        product.setBuyerId(null);
        product.setBuyerStatus(0);
        product.setSellerStatus(0);

        // 保存商品
        return productRepository.save(product);
    }

    //获取用户购买的商品
    public List<Product> getMyPurchases(Long buyerId) {
        return productRepository.findByBuyerId(buyerId);
    }

    //删除商品（只能删除自己的商品）
    public ResponseEntity<Result> deleteProduct(Integer productId, Long sellerId) {
        // 查找商品
        Product product = productRepository.findByProductId(productId).orElse(null);
        if (product == null) {
            return Result.error(404, "商品不存在");
        }

        // 验证是否是自己的商品
        if (!sellerId.equals(product.getSellerId())) {
            return Result.error(403, "只能删除自己的商品");
        }

        // 删除商品
        productRepository.delete(product);
        return Result.success(null, "删除商品成功");
    }

}
