package com.keshe.server.controller;

import com.keshe.server.data.dto.AddCommentDTO;
import com.keshe.server.data.vo.Result;
import com.keshe.server.service.CommentService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    // 根据商品ID获取评论列表
    @GetMapping("/list")
    public ResponseEntity<Result> getCommentsByProductId(@RequestParam("productId") int productId) {
        return commentService.getCommentsByProductId(productId);
    }

    // 添加评论
    @PostMapping("/add")
    public ResponseEntity<Result> addComment(@RequestBody AddCommentDTO dto, @RequestAttribute("userId") Long userId) {
        return commentService.addComment(dto.getProductId(), dto.getContent(), userId);
    }
}
