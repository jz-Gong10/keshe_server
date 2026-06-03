package com.keshe.server.service;

import com.keshe.server.data.dto.AuthDTO;
import com.keshe.server.data.dto.ChangePasswordDTO;
import com.keshe.server.data.po.User;
import com.keshe.server.data.vo.Result;
import com.keshe.server.repository.UserRepository;
import com.keshe.server.utils.BcryptUtils;
import com.keshe.server.utils.JWTUtil;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private JWTUtil jwtUtil;
    
    /**
     * 获取token
     *
     * @param user 用户
     * @return Map<String, String>
     */
    private Map<String, String> getToken(User user) {
        String token;
        String refreshToken;
        String id = user.getId().toString();
        refreshToken = jwtUtil.getToken(id, JWTUtil.REFRESH_EXPIRE_TIME, JWTUtil.REFRESH_SECRET_KEY);

        token = jwtUtil.getToken(id, JWTUtil.EXPIRE_TIME, JWTUtil.SECRET_KEY);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", token);
        tokenMap.put("refreshToken", refreshToken);
        tokenMap.put("username", user.getUsername());
        return tokenMap;
    }

    public ResponseEntity<Result> login(AuthDTO loginReqDTO) {
        String username = loginReqDTO.getUsername();
        String password = loginReqDTO.getPassword();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        if (!BcryptUtils.verifyPasswd(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return Result.success(getToken(user), "登录成功");
    }

    boolean isExisted(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public ResponseEntity<Result> register(AuthDTO registerReqDTO) {
        String username = registerReqDTO.getUsername();
        String password = registerReqDTO.getPassword();
        if (isExisted(username)) {
            throw new RuntimeException("用户已存在");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(BcryptUtils.encrypt(password));
        User savedUser = userRepository.save(user);
        return Result.success(getToken(savedUser), "注册成功");
    }

    @Transactional
    public ResponseEntity<Result> changePassword(ChangePasswordDTO changePasswordReqDTO) {
        String username = changePasswordReqDTO.getUsername();
        String oldPassword = changePasswordReqDTO.getOldPassword();
        String newPassword = changePasswordReqDTO.getNewPassword();
        String confirmNewPassword = changePasswordReqDTO.getConfirmNewPassword();

        // 参数校验
        if (username == null || username.trim().isEmpty()) {
            return Result.error(400, "用户名不能为空");
        }
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            return Result.error(400, "旧密码不能为空");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return Result.error(400, "新密码不能为空");
        }
        if (confirmNewPassword == null || confirmNewPassword.trim().isEmpty()) {
            return Result.error(400, "确认密码不能为空");
        }

        // 新密码与确认密码不一致
        if (!newPassword.equals(confirmNewPassword)) {
            return Result.error(400, "新密码与确认密码不一致");
        }

        // 查找用户
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }

        // 验证旧密码
        if (!BcryptUtils.verifyPasswd(oldPassword, user.getPassword())) {
            return Result.error(400, "旧密码错误");
        }

        // 更新密码
        user.setPassword(BcryptUtils.encrypt(newPassword));
        userRepository.save(user);

        return Result.success(null, "密码修改成功");
    }

}
