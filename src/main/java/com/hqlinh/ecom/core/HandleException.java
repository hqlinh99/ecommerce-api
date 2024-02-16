package com.hqlinh.ecom.core;


import com.hqlinh.ecom.account.AccountException;
import com.hqlinh.ecom.file.FileUploadException;
import com.hqlinh.ecom.product.ProductException;
import com.hqlinh.ecom.util.ValueMapper;
import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class HandleException {

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            ServletRequestBindingException.class,
            HandlerMethodValidationException.class,
            NoHandlerFoundException.class,
            NoResourceFoundException.class,
            AsyncRequestTimeoutException.class,
            ErrorResponseException.class,
            MaxUploadSizeExceededException.class,
            ConversionNotSupportedException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MethodValidationException.class,
            BindException.class,
            MultipartException.class,
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public ResponseEntity<?> handleException(Exception ex) {
        APIResponse<String> response = APIResponse
                .<String>builder()
                .errors(Collections.singletonList(new ErrorResponse(null, ex.getMessage())))
                .status("FAILED")
                .build();
        log.error("HandleException::{} catch error: {}", ex.getClass().getSimpleName(), ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
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

    //
    @ExceptionHandler({
            ProductException.ProductServiceBusinessException.class,
            AccountException.AccountServiceBusinessException.class,
            FileUploadException.FileUploadServiceBusinessException.class
    })
    public ResponseEntity<APIResponse> handleServiceBusinessException(Exception ex) {
        APIResponse<String> response = APIResponse
                .<String>builder()
                .errors(Collections.singletonList(new ErrorResponse(null, ex.getMessage())))
                .status("FAILED")
                .build();
        log.error("{}::{} catch error: {}", ex.getClass().getSimpleName(), ex.getCause().getMessage(), ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<APIResponse> handleNoResultException(Exception ex) {
        APIResponse<String> response = APIResponse
                .<String>builder()
                .errors(Collections.singletonList(new ErrorResponse(null, ex.getMessage())))
                .status("FAILED")
                .build();
        log.error("{}::handleNoResultException catch error: {}", ex.getClass().getSimpleName(), ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomException.DuplicatedException.class)
    public ResponseEntity<APIResponse> handleDuplicatedException(Exception ex) {
        APIResponse<String> response = APIResponse
                .<String>builder()
                .errors(Collections.singletonList(new ErrorResponse(null, ex.getMessage())))
                .status("FAILED")
                .build();
        log.error("{}::handleDuplicatedException catch error: {}", ex.getClass().getSimpleName(), ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(AccountException.NoAccessException.class)
    public ResponseEntity<APIResponse> handleNoAccessException(Exception ex) {
        APIResponse<String> response = APIResponse
                .<String>builder()
                .errors(Collections.singletonList(new ErrorResponse(null, ex.getMessage())))
                .status("FAILED")
                .build();
        log.error("{}::handleNoAccessException catch error: {}", ex.getClass().getSimpleName(), ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
