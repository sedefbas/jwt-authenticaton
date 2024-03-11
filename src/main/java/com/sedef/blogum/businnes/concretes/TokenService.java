package com.sedef.blogum.businnes.concretes;

import com.sedef.blogum.businnes.abstracts.IToken;
import com.sedef.blogum.model.Token;
import com.sedef.blogum.model.TokenType;
import com.sedef.blogum.model.User;
import com.sedef.blogum.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService implements IToken {

   private final TokenRepository repository;

    @Override
    public void addToken(User user, String token) {
        Token savedToken = new Token();
        savedToken.setToken(token);
        savedToken.setUser(user);
        savedToken.setTokenType(TokenType.BEARER);
        savedToken.setExpired(false);
        savedToken.setRevoked(false);
        repository.save(savedToken);
    }

    @Override
    public void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = repository.findAllValidTokenByUser(user.getId());
        if(validUserTokens.isEmpty()){
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true); //süresi doldu
            token.setRevoked(true); //iptal edildi;
        });
        repository.saveAll(validUserTokens);
    }

    @Override
    public String findLastTokenByUserId(Long id) {
        return  repository.findLastTokenByUserId(id);
    }

    @Override
    public Optional<Token> findByToken(String token){
       return repository.findByToken(token);
    }
}
