package org.deliverysystem.com.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.deliverysystem.com.dtos.auth.AuthResponse;
import org.deliverysystem.com.dtos.auth.LoginRequest;
import org.deliverysystem.com.dtos.auth.RefreshResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request, HttpServletResponse response);
    RefreshResponse refresh(HttpServletRequest request);
    void logout(HttpServletResponse response);
}