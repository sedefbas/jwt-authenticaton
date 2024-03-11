package com.sedef.blogum.expections.userExceptions;

public class UserInactiveException extends RuntimeException {
    public UserInactiveException(String message) {
        super(message);
    }
}
