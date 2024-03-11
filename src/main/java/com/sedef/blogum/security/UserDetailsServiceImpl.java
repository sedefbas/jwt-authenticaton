package com.sedef.blogum.security;

import com.sedef.blogum.model.MyUserDetails;
import com.sedef.blogum.model.User;
import com.sedef.blogum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOp = userRepository.findByUserName(username);

        if (!userOp.isPresent() || !userOp.get().getIsEnabled()) {
            throw new UsernameNotFoundException("Could not find active user");
        }
        return new MyUserDetails(userOp.get());
    }

}
