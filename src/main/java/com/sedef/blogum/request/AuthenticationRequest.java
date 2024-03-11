package com.sedef.blogum.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AuthenticationRequest implements Request {
    private String email;

    private String password;

}
