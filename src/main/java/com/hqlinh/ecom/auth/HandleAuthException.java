package com.hqlinh.ecom.auth;


import com.hqlinh.ecom.core.APIResponse;
import com.hqlinh.ecom.core.ErrorResponse;
import com.hqlinh.ecom.util.ValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class HandleAuthException {

    @ExceptionHandler({AuthenticationException.class, NumberFormatException.class})
    public ResponseEntity<APIResponse> handleHandleAuthException(Exception ex) {
        APIResponse<String> response = APIResponse
                .<String>builder()
                .errors(Collections.singletonList(new ErrorResponse(null, ex.getMessage())))
                .status("FAILED")
                .build();
        log.error("{}::handleHandleAuthException catch error: {}", ex.getClass().getSimpleName(), ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
