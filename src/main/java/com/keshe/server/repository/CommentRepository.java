package com.keshe.server.repository;

import com.keshe.server.data.po.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 根据商品ID查询评论
    List<Comment> findByProductId(int productId);
}
