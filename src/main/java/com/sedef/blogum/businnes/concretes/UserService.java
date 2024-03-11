package com.sedef.blogum.businnes.concretes;
import com.sedef.blogum.businnes.abstracts.IUser;
import com.sedef.blogum.expections.userExceptions.GetAllUsersException;
import com.sedef.blogum.expections.userExceptions.UserNotFoundException;
import com.sedef.blogum.model.Token;
import com.sedef.blogum.model.User;
import com.sedef.blogum.repository.UserRepository;
import com.sedef.blogum.request.ChangePasswordRequest;
import com.sedef.blogum.request.RegisterRequest;
import com.sedef.blogum.request.ResetPasswordRequest;
import com.sedef.blogum.security.JwtService;
import com.sedef.blogum.security.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements IUser {

   private final UserRepository userRepository;
   private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SecurityContextUtil securityContextUtil;
    private final TokenService tokenService;
    private final EmailService mailService;

        @Transactional
        @Override
        public User addUser(RegisterRequest request) {
            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setUserName(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setAuthorities(request.getAuthorities());
            user.setAccountNonExpired(true); //süresi dolmadı
            user.setAccountNonLocked(true); // hesap kilitli değil
            user.setCredentialsNonExpired(true); //şifre süresi daha dolmadı.
            user.setIsEnabled(false);  //veri tabanına kaydettik fakat üyelik aktif değil halen.
            try {
               return user = userRepository.save(user);
            } catch (Exception e) {
                throw new UserNotFoundException("User could not be added");
            }
        }

    @Override
        public void deleteUserById(int id) {
        try {
            userRepository.deleteById((long) id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while deleting user with ID " + id, e);
       }
    }


    @Override
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new GetAllUsersException("Error occurred while retrieving all users");
        }
    }


    @Override
    public User findByUserName(String email) {
        Optional<User> userOptional = userRepository.findByUserName(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user;
        } else {
      return null;
        }
    }

    @Override
    public String userActivationLink(String token) {
        String userEmail = jwtService.extractUser(token);
        try {
            enableUser(userEmail);
            return "confirmed";
        } catch (Exception e) {
            e.printStackTrace();
            return "error activaitonlink";
        }
    }

    @Override
    public int enableUser(String email) {
        return userRepository.enableAppUser(email);
    }


    @Override
    @Transactional
    public String changePassword(ChangePasswordRequest request) {
        try {
            String userEmail = securityContextUtil.getCurrentUserEmail();
            User user = findByUserName(userEmail);
            if (user != null && passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                String newPassword = request.getNewPassword();
                String confirmPassword = request.getConfirmationPassword();
                if (newPassword.equals(confirmPassword)) {
                    String hashNewPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(hashNewPassword); // Hashlanmış yeni şifre kullanılmalı
                    userRepository.save(user);
                    return "Password changed successfully";
                } else {
                    throw new IllegalArgumentException("New password and confirmation password do not match");
                }
            } else {
                throw new IllegalArgumentException("Incorrect current password");
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // Hatanın detaylarını konsola yazdırma
            throw new IllegalArgumentException("An error occurred while changing password: " + ex.getMessage());
        }
    }



    @Override
    public String forgetPasswordSend(String email) {
        try {
            User user = findByUserName(email);
            if (user != null) {
                tokenService.revokeAllUserTokens(user);
                String token = jwtService.generateToken(user.getUserName());
                tokenService.addToken(user, token);
                String link = "http://localhost:8080/user/reset-password?token=" + token;
                mailService.send(email, mailService.buildResetEmail(user.getFirstName(), link));
                return "Reset password email sent successfully";
            } else {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while sending reset password email", e);
        }
    }




    @Override
    @Transactional
    public String resetPassword(String token, ResetPasswordRequest request) {
        try {
            Date expirationDate = new Date(System.currentTimeMillis() + 15 * 60 * 1000);
            Optional<Token> tokenOp = tokenService.findByToken(token);
            Date issuedAt = jwtService.extractIssuedAt(token);

            if (issuedAt != null && issuedAt.before(expirationDate)) {
                if (request.getNewPassword().equals(request.getConfirmationPassword())) {
                    String hashNewPassword = passwordEncoder.encode(request.getNewPassword());
                    String userName = jwtService.extractUser(token);
                    User user = findByUserName(userName);
                    if (user != null) {
                        user.setPassword(hashNewPassword);
                        userRepository.save(user);
                        tokenService.revokeAllUserTokens(user);
                        return "Password changed successfully";
                    } else throw new UsernameNotFoundException("User not found with username: " + userName);

                } else throw new IllegalArgumentException("New password and confirmation password do not match");

            } else throw new IllegalArgumentException("Token is not valid or has expired.");

        } catch (Exception ex) {
            ex.printStackTrace();
            return "An error occurred during password reset: " + ex.getMessage(); // Kullanıcıya hata mesajını döndürme
        }
    }



}

