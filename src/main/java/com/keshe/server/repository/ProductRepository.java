package com.keshe.server.repository;
import com.keshe.server.data.po.Comment;
import com.keshe.server.data.po.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByProductId(Integer productId);
    List<Product> findByProductCategory(String productCategory);
    List<Product> findByBuyerId(Long buyerId);
    List<Product> findBySellerId(Long sellerId);
    
    // 获取 buyerStatus 和 sellerStatus 都为 0 的商品
    List<Product> findByBuyerStatusAndSellerStatus(int buyerStatus, int sellerStatus);
    
    // 根据分类获取 buyerStatus 和 sellerStatus 都为 0 的商品
    List<Product> findByProductCategoryAndBuyerStatusAndSellerStatus(String productCategory, int buyerStatus, int sellerStatus);
}
