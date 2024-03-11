package com.sedef.blogum.businnes.abstracts;

import com.sedef.blogum.model.Token;
import com.sedef.blogum.model.User;

import java.util.Optional;

public interface IToken {
    void addToken(User user, String token);
    void revokeAllUserTokens(User user);
    String findLastTokenByUserId(Long id);
    Optional<Token> findByToken(String token);


}
