package com.keshe.server.controller;

import com.keshe.server.data.dto.UpdateProfileDTO;
import com.keshe.server.data.vo.Result;
import com.keshe.server.service.UpdateProfileService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UpdateProfileService updateProfileService;

    @GetMapping("/profile")
    public ResponseEntity<Result> getProfile(@RequestAttribute("userId") Long userId) {
        return updateProfileService.getProfile(userId);
    }

    @PostMapping("/profile")
    public ResponseEntity<Result> updateProfile(@RequestAttribute("userId") Long userId, @ModelAttribute UpdateProfileDTO updateProfileDTO) {
        return updateProfileService.updateProfile(userId, updateProfileDTO);
    }
}
