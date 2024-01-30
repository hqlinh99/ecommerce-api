package com.hqlinh.sachapi.auth;

import com.hqlinh.sachapi.account.Account;
import com.hqlinh.sachapi.account.IAccountRepository;
import com.hqlinh.sachapi.security.CustomAuthenticationManager;
import com.hqlinh.sachapi.util.ValueMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

            Authentication authentication = authenticate.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );

            Account account = (Account) authentication.getPrincipal();
            authenticationResponse = new Auth.AuthenticationResponse(jwtService.generateAccessToken(account), jwtService.generateRefreshToken(account));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("AuthService:create request parameters {}", ValueMapper.jsonAsString(authenticationRequest));
        } catch (AuthException.AuthServiceBusinessException ex) {
            log.error("Exception occurred while logging in account, Exception message {}", ex.getMessage());
            throw new AuthException.AuthServiceBusinessException("Exception occurred while logging in account!");
        } catch (UsernameNotFoundException ex) {
            log.error("Exception occurred while logging in account, Exception message {}", ex.getMessage());
            throw new UsernameNotFoundException(ex.getMessage());
        }

        log.info("AuthService::login execution ended...");
        return authenticationResponse;
    }

    public Auth.AuthenticationResponse refreshToken(String refreshToken) {
        Auth.AuthenticationResponse authenticationResponse;
        try {
            log.info("AuthService::refresh token execution started...");

            String username = jwtService.getUsernameFromToken(refreshToken);

            log.debug("AuthService:refresh token request parameters {}", refreshToken);
            Account account = accountRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

            authenticationResponse = new Auth.AuthenticationResponse(jwtService.generateAccessToken(account), jwtService.generateRefreshToken(account));
            log.debug("AuthService:refresh token request parameters {}", refreshToken);
        } catch (AuthException.AuthServiceBusinessException ex) {
            log.error("Exception occurred while logging in account, Exception message {}", ex.getMessage());
            throw new AuthException.AuthServiceBusinessException("Exception occurred while logging in account!");
        }

        log.info("AuthService::refresh token execution ended...");
        return authenticationResponse;
    }
}
