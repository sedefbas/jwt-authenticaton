package com.sedef.blogum.request;

import com.sedef.blogum.model.Role;
import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
public class RegisterRequest implements Request {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    Set<Role> authorities;

}
