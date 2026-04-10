package com.keshe.server.service;

import com.keshe.server.data.dto.UpdateProfileDTO;
import com.keshe.server.data.po.User;
import com.keshe.server.data.vo.Result;
import com.keshe.server.repository.UserRepository;
import com.keshe.server.repository.UpdateProfileRepository;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateProfileService implements UpdateProfileRepository {

    @Resource
    private UserRepository userRepository;

    @Override
    public ResponseEntity<Result> getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
//        // 隐藏密码等敏感信息
//        user.setPassword(null);
        return Result.success(user, "获取个人资料成功");
    }

    @Override
    @Transactional
    public ResponseEntity<Result> updateProfile(Long userId, UpdateProfileDTO updateProfileDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 更新个人资料
        if (updateProfileDTO.getNickname() != null) {
            user.setNickname(updateProfileDTO.getNickname());
        }
        if (updateProfileDTO.getAvatar() != null) {
            user.setAvatar(updateProfileDTO.getAvatar());
        }
        if (updateProfileDTO.getEmail() != null) {
            user.setEmail(updateProfileDTO.getEmail());
        }
        if (updateProfileDTO.getPhone() != null) {
            user.setPhone(updateProfileDTO.getPhone());
        }
        if (updateProfileDTO.getGender() != null) {
            user.setGender(updateProfileDTO.getGender());
        }
        if (updateProfileDTO.getBio() != null) {
            user.setBio(updateProfileDTO.getBio());
        }

        userRepository.save(user);
        // 隐藏密码等敏感信息
//        user.setPassword(null);
        return Result.success(user, "更新个人资料成功");
    }


}
