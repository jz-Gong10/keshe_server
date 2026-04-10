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
        if(!newPassword.equals(confirmNewPassword)){
            throw new RuntimeException("新密码与确认密码不一致");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        if(!BcryptUtils.verifyPasswd(oldPassword, user.getPassword())){
            throw new RuntimeException("旧密码错误");
        }
        user.setPassword(BcryptUtils.encrypt(newPassword));
        userRepository.save(user);
        return Result.success(getToken(user),"密码修改成功");
    }

}
