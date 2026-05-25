package com.keshe.server.controller;

import com.keshe.server.data.dto.DeleteCommentDTO;
import com.keshe.server.data.dto.DeleteProductDTO;
import com.keshe.server.data.dto.DeleteUserDTO;
import com.keshe.server.data.vo.Result;
import com.keshe.server.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    // 管理员删除任意评论
    @PostMapping("/comment/delete")
    public ResponseEntity<Result> deleteComment(@RequestAttribute(value = "userId", required = false) Long userId, @RequestBody DeleteCommentDTO dto) {
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        return adminService.deleteCommentAsAdmin(userId, dto.getId());
    }

    // 管理员删除任意商品
    @PostMapping("/product/delete")
    public ResponseEntity<Result> deleteProduct(@RequestAttribute(value = "userId", required = false) Long userId, @RequestBody DeleteProductDTO dto) {
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        return adminService.deleteProductAsAdmin(userId, dto.getProductId());
    }

    // 管理员删除任意用户
    @PostMapping("/user/delete")
    public ResponseEntity<Result> deleteUser(@RequestAttribute(value = "userId", required = false) Long userId, @RequestBody DeleteUserDTO dto) {
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        return adminService.deleteUserAsAdmin(userId, dto.getId());
    }
}
