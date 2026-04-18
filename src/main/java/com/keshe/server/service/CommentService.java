package com.keshe.server.service;

import com.keshe.server.data.po.Comment;
import com.keshe.server.data.vo.Result;
import com.keshe.server.repository.CommentRepository;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Resource
    private CommentRepository commentRepository;

    // 根据商品ID获取评论列表
    public ResponseEntity<Result> getCommentsByProductId(Integer productId) {
        List<Comment> comments = commentRepository.findByProductId(productId);
        return Result.success(comments, "获取评论成功");
    }

    // 添加评论
    public ResponseEntity<Result> addComment(Integer productId, String content, Long userId) {
        Comment comment = new Comment();
        comment.setId(userId);
        comment.setProductId(productId);
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setCreateTime(LocalDateTime.now());
        
        commentRepository.save(comment);
        return Result.success(comment, "添加评论成功");
    }
}

