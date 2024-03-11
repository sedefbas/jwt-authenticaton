package com.sedef.blogum.controller;

import com.sedef.blogum.businnes.concretes.AuthenticationService;
import com.sedef.blogum.expections.userExceptions.UserAlreadyExistsException;
import com.sedef.blogum.expections.userExceptions.UserInactiveException;
import com.sedef.blogum.expections.userExceptions.UserNotFoundException;
import com.sedef.blogum.request.AuthenticationRequest;
import com.sedef.blogum.request.RegisterRequest;
import com.sedef.blogum.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request)  {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
            AuthenticationResponse response = service.login(request);
            return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
            service.refreshToken(request, response);
        return ResponseEntity.noContent().build();
        }
    }

