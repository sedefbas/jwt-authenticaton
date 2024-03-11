package com.sedef.blogum.expections.userExceptions;

public class GetAllUsersException extends RuntimeException {
    public GetAllUsersException(String message) {
        super(message);
    }
}