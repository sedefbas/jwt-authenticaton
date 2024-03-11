package com.sedef.blogum.security;

import com.sedef.blogum.model.MyUserDetails;
import com.sedef.blogum.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
//bu sınıf oturum açıkken o kişinin kimliğini tanımlıyor. çokta complex birşeyi yok.
// securityContex biz zaten kaydetmiştik. kimlikleri ordan alıyor.

@Component
public class SecurityContextUtil {

    public String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof MyUserDetails) {
            return ((MyUserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}