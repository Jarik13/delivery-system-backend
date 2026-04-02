package org.deliverysystem.com.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.deliverysystem.com.dtos.auth.AuthResponse;
import org.deliverysystem.com.dtos.auth.LoginRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request, HttpServletResponse response);
    AuthResponse refresh(HttpServletRequest request, HttpServletResponse response);
    void forgotPassword(String email);
    void logout(HttpServletResponse response);
}