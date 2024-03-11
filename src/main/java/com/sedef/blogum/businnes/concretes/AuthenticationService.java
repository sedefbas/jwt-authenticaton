package com.sedef.blogum.businnes.concretes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sedef.blogum.businnes.abstracts.IAuthentication;
import com.sedef.blogum.businnes.validators.MailValidator;
import com.sedef.blogum.expections.tokenExpections.TokenExceptions;
import com.sedef.blogum.expections.userExceptions.UserAlreadyExistsException;
import com.sedef.blogum.expections.userExceptions.UserInactiveException;
import com.sedef.blogum.expections.userExceptions.UserNotFoundException;
import com.sedef.blogum.model.MyUserDetails;
import com.sedef.blogum.model.User;
import com.sedef.blogum.request.AuthenticationRequest;
import com.sedef.blogum.request.RegisterRequest;
import com.sedef.blogum.response.AuthenticationResponse;
import com.sedef.blogum.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService implements IAuthentication {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final MailValidator mailValidator;


    @Transactional
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        AuthenticationResponse response = new  AuthenticationResponse();

        if (!mailValidator.test(request.getEmail())) {
            throw new IllegalArgumentException("Email is not written in the correct format.");
        }

       if(userService.findByUserName(request.getEmail()) != null)
       {throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists"); }

       User savedUser = userService.addUser(request);
       String token =jwtService.generateToken(request.getEmail());
       String refreshToken = jwtService.generateRefreshToken(request.getEmail());
       tokenService.addToken(savedUser,token);

        String link = "http://localhost:8080/user/confirm?token=" + token;
        emailService.send(request.getEmail(), emailService.buildAuthEmail(request.getFirstName(), link));

       response.setAccessToken(token);
       response.setRefreshToken(refreshToken);
       response.setMessage("kayit basarili");
       response.setUserId(savedUser.getId());
       return response;
    }


    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {

        User user = userService.findByUserName(request.getEmail());

        if (user == null)
            throw new UserNotFoundException("User with email " + request.getEmail() + " not found.");

        if (!user.getIsEnabled())
            throw new UserInactiveException("User with email " + request.getEmail() + " is inactive.");

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            String token = jwtService.generateToken(request.getEmail());
            String refreshToken = jwtService.generateRefreshToken(request.getEmail());

            tokenService.revokeAllUserTokens(user);
            tokenService.addToken(user, token);

            AuthenticationResponse response = new AuthenticationResponse();
            response.setAccessToken("Bearer " + token);
            response.setMessage("Giriş başarılı");
            response.setUserId(user.getId());
            response.setRefreshToken("Bearer " + refreshToken);

            return response;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password.");
        }
    }


    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return ;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUser(refreshToken);
        if (userEmail != null) {
            User user = this.userService.findByUserName(userEmail);
            MyUserDetails userDetails = new MyUserDetails(user);

            if (jwtService.validateToken(refreshToken,userDetails)) {
                var accessToken = jwtService.generateToken(user.getUserName());
                tokenService.revokeAllUserTokens(user);
                tokenService.addToken(user, accessToken);

                AuthenticationResponse authResponse = new AuthenticationResponse();
                authResponse.setAccessToken(accessToken);
                authResponse.setRefreshToken(refreshToken);
                authResponse.setMessage("giris basarili");
                authResponse.setUserId(user.getId());

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }else throw new TokenExceptions("token invalid");
        }else throw new UserNotFoundException("user not found");
    }
}
