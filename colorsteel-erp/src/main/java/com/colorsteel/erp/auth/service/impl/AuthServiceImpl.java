package com.colorsteel.erp.auth.service.impl;

import com.colorsteel.erp.auth.dto.LoginRequest;
import com.colorsteel.erp.auth.dto.LoginResponse;
import com.colorsteel.erp.auth.service.AuthService;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.security.JwtProperties;
import com.colorsteel.erp.common.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String DEMO_USER = "admin";

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;

    /**
     * BCrypt hash for password {@code admin123}（骨架演示，后续接用户表后删除）。
     */
    private static final String DEMO_HASH =
            "$2a$10$LckQehrSgXw6e8ySO6JPaOSSeJL9RPttU355EXnx/J0etVr2IlZTy";

    public AuthServiceImpl(PasswordEncoder passwordEncoder, JwtUtil jwtUtil, JwtProperties jwtProperties) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        if (!DEMO_USER.equals(request.getUsername())
                || !passwordEncoder.matches(request.getPassword(), DEMO_HASH)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "用户名或密码错误");
        }
        String token = jwtUtil.createToken(DEMO_USER, Map.of("role", "ADMIN"));
        return new LoginResponse(token, jwtProperties.getExpirationMs());
    }
}
