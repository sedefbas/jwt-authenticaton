package com.sedef.blogum.businnes.validators;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class MailValidator implements Predicate<String> {

    @Override
    public boolean test(String email) {
        // E-posta adresi için geçerli bir regex deseni
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
