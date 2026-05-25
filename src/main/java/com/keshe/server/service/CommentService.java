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
        comment.setProductId(productId);
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setCreateTime(LocalDateTime.now());

        commentRepository.save(comment);
        return Result.success(comment, "添加评论成功");
    }

    // 删除评论（只能删除自己的评论）
    public ResponseEntity<Result> deleteComment(Long commentId, Long userId) {
        // 查找评论
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            return Result.error(404, "评论不存在");
        }

        // 验证是否是自己的评论
        if (!userId.equals(comment.getUserId())) {
            return Result.error(403, "只能删除自己的评论");
        }

        // 删除评论
        commentRepository.delete(comment);
        return Result.success(null, "删除评论成功");
    }


}

