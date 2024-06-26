package com.hqlinh.ecom.auth;

import com.hqlinh.ecom.account.Account;
import com.hqlinh.ecom.account.IAccountRepository;
import com.hqlinh.ecom.security.CustomAuthenticationManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private CustomAuthenticationManager authenticate;
    private IAccountRepository accountRepository;
    private JWTService jwtService;

    public Auth.AuthenticationResponse login(Auth.AuthenticationRequest authenticationRequest) {
        Auth.AuthenticationResponse authenticationResponse;
        try {
            log.info("AuthService::login execution started...");

            String username = authenticationRequest.getUsername() != null ? authenticationRequest.getUsername() : authenticationRequest.getEmail();
            Authentication authentication = authenticate.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            authenticationRequest.getPassword()
                    )
            );

            Account account = (Account) authentication.getPrincipal();
            authenticationResponse = new Auth.AuthenticationResponse(jwtService.generateAccessToken(account), jwtService.generateRefreshToken(account));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthException.AuthServiceBusinessException | UsernameNotFoundException ex) {
            log.error("Exception occurred while logging in account, Exception message {}", ex.getMessage());
            throw ex;
        }

        log.info("AuthService::login execution ended...");
        return authenticationResponse;
    }

    public Auth.AuthenticationResponse refreshToken(String refreshToken) {
        Auth.AuthenticationResponse authenticationResponse;
        try {
            log.info("AuthService::refresh token execution started...");

            String subject = jwtService.getSubjectToken(refreshToken);

            Account account = accountRepository.findById(Long.parseLong(subject))
                            .orElseThrow(() -> new UsernameNotFoundException("Account not found!"));

            authenticationResponse = new Auth.AuthenticationResponse(jwtService.generateAccessToken(account), jwtService.generateRefreshToken(account));
        } catch (AuthException.AuthServiceBusinessException ex) {
            log.error("Exception occurred while logging in account, Exception message {}", ex.getMessage());
            throw ex;
        }

        log.info("AuthService::refresh token execution ended...");
        return authenticationResponse;
    }
}
