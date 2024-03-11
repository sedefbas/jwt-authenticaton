package com.sedef.blogum.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ResetPasswordRequest {
    private String newPassword;
    private String confirmationPassword;
}
