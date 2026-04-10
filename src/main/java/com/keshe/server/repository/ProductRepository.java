package com.keshe.server.repository;
import com.keshe.server.data.po.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByProductId(int productId);
    List<Product> findByProductCategory(String productCategory);
}
