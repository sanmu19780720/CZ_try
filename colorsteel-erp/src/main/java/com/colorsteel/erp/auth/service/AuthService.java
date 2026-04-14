package com.colorsteel.erp.auth.service;

import com.colorsteel.erp.auth.dto.LoginRequest;
import com.colorsteel.erp.auth.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}
