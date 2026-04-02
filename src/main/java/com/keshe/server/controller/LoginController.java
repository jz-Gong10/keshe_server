package com.keshe.server.controller;

import com.keshe.server.data.dto.AuthDTO;
import com.keshe.server.data.dto.ChangePasswordDTO;
import com.keshe.server.data.vo.Result;
import com.keshe.server.service.LoginService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<Result> register(@RequestBody AuthDTO registerReqDTO) {
        return loginService.register(registerReqDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<Result> login(@RequestBody AuthDTO loginReqDTO) {
        return loginService.login(loginReqDTO);
    }

    @PostMapping("/change_password")
    public ResponseEntity<Result> changePassword(@RequestBody ChangePasswordDTO changePasswordReqDTO) {
        return loginService.changePassword(changePasswordReqDTO);
    }
}
