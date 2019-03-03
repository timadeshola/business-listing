package com.business.resources;

import com.business.core.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.UnknownHostException;

@RestControllerAdvice
@Slf4j
public class ApplicationControllerAdvice {

    @Bean
    public ErrorAttributes errorAttributes() {
        // Hide exception field in the return object
        return new DefaultErrorAttributes();
    }

    @ExceptionHandler(CustomException.class)
    public void handleCustomException(HttpServletResponse res, CustomException ex) throws IOException {
        res.sendError(ex.getHttpStatus().value(), ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(HttpServletResponse res, Exception e) throws IOException {
        res.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
        log.error("Access denied ==> {}", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletResponse res, Exception e) throws IOException {
        res.sendError(HttpStatus.BAD_REQUEST.value(), "Something went wrong, check you request");
        log.error("Something went wrong, check you request ==> {}", e.getMessage());
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public void handleAuthenticationException(HttpServletResponse res, Exception e) throws IOException {
        res.sendError(HttpStatus.UNAUTHORIZED.value(), "Authentication Required");
        log.error("Authentication Required ==> {}", e.getMessage());
    }

    @ExceptionHandler(LazyInitializationException.class)
    public void handleLazyInitializationException(HttpServletResponse res, Exception e) throws IOException {
        res.sendError(HttpStatus.PRECONDITION_REQUIRED.value(), "Error trying to load entity with Lazy Initialization");
        log.error("Error trying to load entity with Lazy Initialization ==> {}", e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(HttpServletResponse res, Exception e) throws IOException {
        res.sendError(HttpStatus.PRECONDITION_REQUIRED.value(), "Illegal Argument");
        log.error("Illegal Argument ==> {}", e.getMessage());
    }

    @ExceptionHandler(UnknownHostException.class)
    public void handleUnknownHostException(HttpServletResponse res, Exception e) throws IOException {
        res.sendError(HttpStatus.REQUEST_TIMEOUT.value(), "Request timed out / bad connections");
        log.error("Request timed out / bad connections ==> {}", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected void handleMethodArgumentNotValid(HttpServletResponse res, Exception e) throws IOException {
        res.sendError(HttpStatus.REQUEST_TIMEOUT.value(), "Validation Failed");
        log.error("Validation Failed ==> {}", e.getMessage());
    }
}
