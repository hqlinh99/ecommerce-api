package com.hqlinh.sachapi.core;


import com.hqlinh.sachapi.account.AccountException;
import com.hqlinh.sachapi.product.ProductException;
import com.hqlinh.sachapi.util.ValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
public class HandleException {

    @ExceptionHandler({HttpMessageNotReadableException.class, SQLException.class, HttpRequestMethodNotSupportedException.class, IllegalArgumentException.class})
    public ResponseEntity<APIResponse> handleHttpMessageNotReadableException(Exception ex) {
        APIResponse<String> response = APIResponse
                .<String>builder()
                .errors(Collections.singletonList(new ErrorResponse(null, ex.getMessage())))
                .status("FAILED")
                .build();
        log.error("HandleException::{} catch error: {}", ex.getClass().getSimpleName(), ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorResponse> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    String field = error.getField().getClass().getSimpleName() + " " + error.getField();
                    errors.add(new ErrorResponse(field, error.getDefaultMessage()));
                });
        APIResponse<String> response = APIResponse
                .<String>builder()
                .errors(errors)
                .status("FAILED")
                .build();
        log.error("{}::handleMethodArgumentNotValidException catch error: {}", ex.getObjectName(), ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            ProductException.ProductServiceBusinessException.class,
            AccountException.AccountServiceBusinessException.class})
    public ResponseEntity<APIResponse> handleProductServiceBusinessException(Exception ex) {
        APIResponse<String> response = APIResponse
                .<String>builder()
                .errors(Collections.singletonList(new ErrorResponse(null, ex.getMessage())))
                .status("FAILED")
                .build();
        log.error("{}::handleProductServiceBusinessException catch error: {}", ex.getClass().getSimpleName(), ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            ProductException.ProductNotFoundException.class,
            AccountException.AccountNotFoundException.class})
    public ResponseEntity<APIResponse> handleProductNotFoundException(Exception ex) {
        APIResponse<String> response = APIResponse
                .<String>builder()
                .errors(Collections.singletonList(new ErrorResponse(null, ex.getMessage())))
                .status("FAILED")
                .build();
        log.error("{}::handleProductNotFoundException catch error: {}", ex.getClass().getSimpleName(), ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
