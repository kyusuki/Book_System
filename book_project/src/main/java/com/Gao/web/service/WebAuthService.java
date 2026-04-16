package com.Gao.web.service;

import com.Gao.dao.UserDao;
import com.Gao.entity.User;
import com.Gao.view.Register;
import com.Gao.util.VerificationCode;
import com.Gao.web.dto.ForgotPasswordRequest;
import com.Gao.web.dto.LoginRequest;
import com.Gao.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class WebAuthService {

    private static final String CAPTCHA_KEY = "webCaptcha";
    private static final String FAIL_USER_KEY = "webLoginFailUsername";
    private static final String FAIL_COUNT_KEY = "webLoginFailCount";
    private static final int MAX_FAILS = 3;

    private final UserDao userDao = new UserDao();
    private final VerificationCode verificationCode = new VerificationCode();

    public String newCaptcha(HttpSession session) {
        String code = verificationCode.getCode();
        session.setAttribute(CAPTCHA_KEY, code);
        return code;
    }

    public boolean captchaMatches(HttpSession session, String input) {
        if (input == null || input.isBlank()) {
            return false;
        }
        Object raw = session.getAttribute(CAPTCHA_KEY);
        if (!(raw instanceof String expected)) {
            return false;
        }
        return expected.equalsIgnoreCase(input.trim());
    }

    public User login(HttpSession session, LoginRequest req) {
        if (!captchaMatches(session, req.getCaptcha())) {
            return null;
        }
        session.removeAttribute(CAPTCHA_KEY);

        String username = req.getUsername() == null ? "" : req.getUsername().trim();
        User user = userDao.getUserWithUsername(username);
        if (user == null) {
            return null;
        }

        String failUser = (String) session.getAttribute(FAIL_USER_KEY);
        Integer failCount = (Integer) session.getAttribute(FAIL_COUNT_KEY);
        if (failCount == null) {
            failCount = 0;
        }
        if (!username.equals(failUser)) {
            failUser = username;
            failCount = 0;
        }

        if (failCount >= MAX_FAILS) {
            session.setAttribute(FAIL_USER_KEY, failUser);
            session.setAttribute(FAIL_COUNT_KEY, failCount);
            throw new IllegalStateException("登录失败次数过多，请稍后重试");
        }

        if (!userDao.verifyPassword(username, req.getPassword())) {
            failCount++;
            session.setAttribute(FAIL_USER_KEY, failUser);
            session.setAttribute(FAIL_COUNT_KEY, failCount);
            return null;
        }

        session.setAttribute(FAIL_USER_KEY, username);
        session.setAttribute(FAIL_COUNT_KEY, 0);
        return user;
    }

    public String validateAndRegister(RegisterRequest r) {
        String username = r.getUsername() == null ? "" : r.getUsername().trim();
        if (username.length() < 3 || username.length() > 15) {
            return "用户名长度必须在3-15之间";
        }
        int letter = 0;
        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') {
                letter++;
            }
        }
        if (letter == 0) {
            return "用户名必须包含一个字母";
        }
        if (!Register.isLetterDigit(username)) {
            // 与 Register.register 控制台提示保持一致
            return "用户名必须包含字母或数字";
        }
        if (userDao.isExist(username)) {
            return "用户名已存在";
        }
        if (r.getPassword() == null || r.getPasswordConfirm() == null
                || !r.getPassword().equals(r.getPasswordConfirm())) {
            return "两次密码不一致";
        }
        String id = r.getIdCard() == null ? "" : r.getIdCard().trim();
        if (!Register.isId(id)) {
            return "身份证号格式错误";
        }
        String phone = r.getPhone() == null ? "" : r.getPhone().trim();
        if (!Register.isPhone(phone)) {
            return "手机号格式错误";
        }

        User u = new User();
        u.setUsername(username);
        u.setPassword(r.getPassword());
        u.setId(id);
        u.setPhone(phone);
        u.setUserType(0);
        if (!userDao.addUser(u)) {
            return "注册失败";
        }
        return null;
    }

    public boolean resetPassword(ForgotPasswordRequest r) {
        String username = r.getUsername() == null ? "" : r.getUsername().trim();
        String id = r.getIdCard() == null ? "" : r.getIdCard().trim();
        String phone = r.getPhone() == null ? "" : r.getPhone().trim();
        if (!userDao.verifyIdentity(username, id, phone)) {
            return false;
        }
        return userDao.updatePassword(username, r.getNewPassword());
    }
}
