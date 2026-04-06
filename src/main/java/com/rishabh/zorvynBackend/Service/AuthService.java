package com.rishabh.zorvynBackend.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.rishabh.zorvynBackend.DTO.request.LoginRequest;
import com.rishabh.zorvynBackend.DTO.request.RegisterRequest;
import com.rishabh.zorvynBackend.DTO.response.AuthResponse;
import com.rishabh.zorvynBackend.Entity.User;
import com.rishabh.zorvynBackend.Enums.Role;
import com.rishabh.zorvynBackend.Exception.DuplicateEmailException;
import com.rishabh.zorvynBackend.Repository.UserRepository;
import com.rishabh.zorvynBackend.Security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    @Value("${app.admin-secret:}")
    private String adminSecret;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + request.getEmail());
        }

        // only admin can register;
        if (request.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only ADMIN registration is allowed");
        }

        if (adminSecret == null || adminSecret.isBlank() || !adminSecret.equals(request.getSecretCode())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid admin secret code");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ADMIN);

        userRepository.save(user);
        // login
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User authenticatedUser)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authentication principal");
        }
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtService.generateToken(authenticatedUser));
        return authResponse;

    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authentication principal");
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtService.generateToken(user));
        return authResponse;
    }
}