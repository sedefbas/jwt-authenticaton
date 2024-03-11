package com.sedef.blogum.businnes.abstracts;

import com.sedef.blogum.request.AuthenticationRequest;
import com.sedef.blogum.request.RegisterRequest;
import com.sedef.blogum.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

public interface IAuthentication {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse login(AuthenticationRequest request) throws AuthenticationException;
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
