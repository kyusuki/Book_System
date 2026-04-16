package com.Gao.web.config;

import com.Gao.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.Gao.web.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

@Component
public class SessionAuthInterceptor implements HandlerInterceptor {
    public static final String SESSION_USER_KEY = "sessionUser";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute(SESSION_USER_KEY);
        if (user != null) {
            return true;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json;charset=UTF-8");
        String body = objectMapper.writeValueAsString(ApiResponse.fail("请先登录"));
        response.getWriter().write(body);
        return false;
    }
}
