package com.keshe.server.service;

import com.keshe.server.data.po.Comment;
import com.keshe.server.data.po.Product;
import com.keshe.server.data.po.User;
import com.keshe.server.data.vo.Result;
import com.keshe.server.repository.CommentRepository;
import com.keshe.server.repository.ProductRepository;
import com.keshe.server.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Resource
    private CommentRepository commentRepository;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private UserRepository userRepository;

    // 验证是否为管理员
    private ResponseEntity<Result> checkAdmin(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        // 检查角色是否为管理员（role = "ADMIN"）
        if (!"ADMIN".equals(user.getRole())) {
            return Result.error(403, "无管理员权限");
        }
        return null; // 验证通过
    }

    // 管理员删除任意评论
    public ResponseEntity<Result> deleteCommentAsAdmin(Long userId, Long commentId) {
        // 验证管理员权限
        ResponseEntity<Result> checkResult = checkAdmin(userId);
        if (checkResult != null) {
            return checkResult;
        }

        // 查找评论
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            return Result.error(404, "评论不存在");
        }

        // 删除评论
        commentRepository.delete(comment);
        return Result.success(null, "删除评论成功");
    }

    // 管理员删除任意商品
    public ResponseEntity<Result> deleteProductAsAdmin(Long userId, Integer productId) {
        // 验证管理员权限
        ResponseEntity<Result> checkResult = checkAdmin(userId);
        if (checkResult != null) {
            return checkResult;
        }

        // 查找商品
        Product product = productRepository.findByProductId(productId).orElse(null);
        if (product == null) {
            return Result.error(404, "商品不存在");
        }

        // 删除商品
        productRepository.delete(product);
        return Result.success(null, "删除商品成功");
    }

    // 管理员删除任意用户
    public ResponseEntity<Result> deleteUserAsAdmin(Long adminId, Long targetUserId) {
        // 验证管理员权限
        ResponseEntity<Result> checkResult = checkAdmin(adminId);
        if (checkResult != null) {
            return checkResult;
        }

        // 查找目标用户
        User targetUser = userRepository.findById(targetUserId).orElse(null);
        if (targetUser == null) {
            return Result.error(404, "用户不存在");
        }

        // 删除用户
        userRepository.delete(targetUser);
        return Result.success(null, "删除用户成功");
    }
}
