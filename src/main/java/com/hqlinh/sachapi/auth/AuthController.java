package com.hqlinh.sachapi.auth;

import com.hqlinh.sachapi.core.APIResponse;
import com.hqlinh.sachapi.product.ProductDTO;
import com.hqlinh.sachapi.util.ValueMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
@CrossOrigin
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Auth.AuthenticationRequest authenticationRequest) {
        log.info("AuthController::login with input {}", ValueMapper.jsonAsString(authenticationRequest));
        Auth.AuthenticationResponse authenticationResponse = authService.login(authenticationRequest);
        APIResponse<Auth.AuthenticationResponse> response = APIResponse
                .<Auth.AuthenticationResponse>builder()
                .status("SUCCESS")
                .result(authenticationResponse)
                .build();
        log.info("AuthController::login response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> token) {
        log.info("AuthController::refresh token with input {}", ValueMapper.jsonAsString(token));
        Auth.AuthenticationResponse authenticationResponse = authService.refreshToken(token.get("refreshToken"));
        APIResponse<Auth.AuthenticationResponse> response = APIResponse
                .<Auth.AuthenticationResponse>builder()
                .status("SUCCESS")
                .result(authenticationResponse)
                .build();
        log.info("AuthController::refresh token response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
