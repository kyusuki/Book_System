package com.Gao.web.controller;

import com.Gao.entity.User;
import com.Gao.web.config.SessionAuthInterceptor;
import com.Gao.web.dto.ApiResponse;
import com.Gao.web.dto.ForgotPasswordRequest;
import com.Gao.web.dto.LoginRequest;
import com.Gao.web.dto.RegisterRequest;
import com.Gao.web.dto.UserDto;
import com.Gao.web.service.WebAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final WebAuthService webAuthService;

    public AuthController(WebAuthService webAuthService) {
        this.webAuthService = webAuthService;
    }

    @GetMapping("/captcha")
    public ApiResponse<Map<String, String>> captcha(HttpSession session) {
        String code = webAuthService.newCaptcha(session);
        return ApiResponse.ok(Map.of("captcha", code));
    }

    @PostMapping("/login")
    public ApiResponse<UserDto> login(@RequestBody LoginRequest req, HttpSession session) {
        try {
            User user = webAuthService.login(session, req);
            if (user == null) {
                return ApiResponse.fail("用户名不存在或密码错误");
            }
            session.setAttribute(SessionAuthInterceptor.SESSION_USER_KEY, user);
            return ApiResponse.ok("登录成功", UserDto.fromEntity(user));
        } catch (IllegalStateException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        session.invalidate();
        return ApiResponse.ok("已退出", null);
    }

    @GetMapping("/me")
    public ApiResponse<UserDto> me(HttpSession session) {
        User u = (User) session.getAttribute(SessionAuthInterceptor.SESSION_USER_KEY);
        if (u == null) {
            return ApiResponse.fail("未登录");
        }
        return ApiResponse.ok(UserDto.fromEntity(u));
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody RegisterRequest req) {
        String err = webAuthService.validateAndRegister(req);
        if (err != null) {
            return ApiResponse.fail(err);
        }
        return ApiResponse.ok("注册成功", null);
    }

    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgot(@RequestBody ForgotPasswordRequest req) {
        if (req.getNewPassword() == null || req.getNewPassword().isBlank()) {
            return ApiResponse.fail("新密码不能为空");
        }
        if (!webAuthService.resetPassword(req)) {
            return ApiResponse.fail("账号信息不一致或修改失败");
        }
        return ApiResponse.ok("密码已重置", null);
    }
}
