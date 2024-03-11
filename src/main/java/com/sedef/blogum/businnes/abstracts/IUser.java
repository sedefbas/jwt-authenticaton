package com.sedef.blogum.businnes.abstracts;
import com.sedef.blogum.model.User;
import com.sedef.blogum.request.ChangePasswordRequest;
import com.sedef.blogum.request.RegisterRequest;
import com.sedef.blogum.request.ResetPasswordRequest;

import java.util.List;


public interface IUser {
    User addUser(RegisterRequest request);
    void deleteUserById(int Id);
    List<User> getAllUsers();

    User findByUserName(String email);

    String userActivationLink(String token );

    int enableUser(String email);

    String changePassword(ChangePasswordRequest request);
    String forgetPasswordSend(String email);
    String resetPassword(String token, ResetPasswordRequest request);




}
