package com.sedef.blogum.expections;

import com.sedef.blogum.expections.tokenExpections.TokenExceptions;
import com.sedef.blogum.expections.userExceptions.GetAllUsersException;
import com.sedef.blogum.expections.userExceptions.UserAlreadyExistsException;
import com.sedef.blogum.expections.userExceptions.UserInactiveException;
import com.sedef.blogum.expections.userExceptions.UserNotFoundException;
import com.sedef.blogum.response.AppErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<AppErrorResponse> handleException(UserNotFoundException ex) {
        AppErrorResponse errorResponse = prepareErrorResponse(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(GetAllUsersException.class)
    public ResponseEntity<AppErrorResponse> handleException(GetAllUsersException ex) {
        AppErrorResponse errorResponse = prepareErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<AppErrorResponse> handleException(UserAlreadyExistsException ex) {
        AppErrorResponse errorResponse = prepareErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(UserInactiveException.class)
    public ResponseEntity<AppErrorResponse> handleException(UserInactiveException ex) {
        AppErrorResponse errorResponse = prepareErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    //bunun için sınıf yazmadım. otomatik olarak vardı zaten.
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AppErrorResponse> handleException(BadCredentialsException ex) {
        AppErrorResponse errorResponse = prepareErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(TokenExceptions.class)
    public ResponseEntity<AppErrorResponse> handleTokenNotFoundException(TokenExceptions ex) {
        AppErrorResponse errorResponse = prepareErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }


    private AppErrorResponse prepareErrorResponse(HttpStatus badRequest, String exceptionMessage) {
        AppErrorResponse response = new AppErrorResponse();
        response.setStatus(badRequest.value());
        response.setMessage(exceptionMessage);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }
}
