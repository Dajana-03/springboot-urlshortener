package com.dajanapirjasi.UrlShortener.controller;

import com.dajanapirjasi.UrlShortener.model.dto.AuthRequest;
import com.dajanapirjasi.UrlShortener.model.dto.AuthResponse;
import com.dajanapirjasi.UrlShortener.model.dto.RegisterRequest;
import com.dajanapirjasi.UrlShortener.service.jwt.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}








