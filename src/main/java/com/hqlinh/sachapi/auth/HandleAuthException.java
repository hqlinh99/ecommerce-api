package com.hqlinh.sachapi.auth;


import com.hqlinh.sachapi.account.AccountException;
import com.hqlinh.sachapi.core.APIResponse;
import com.hqlinh.sachapi.core.ErrorResponse;
import com.hqlinh.sachapi.product.ProductException;
import com.hqlinh.sachapi.util.ValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class HandleAuthException {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<APIResponse> handleHandleAuthException(AuthenticationException ex) {
        APIResponse<String> response = APIResponse
                .<String>builder()
                .errors(Collections.singletonList(new ErrorResponse(null, ex.getMessage())))
                .status("FAILED")
                .build();
        log.error("{}::handleHandleAuthException catch error: {}", ex.getClass().getSimpleName(), ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
