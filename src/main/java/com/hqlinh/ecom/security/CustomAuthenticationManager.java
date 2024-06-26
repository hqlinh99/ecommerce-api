package com.hqlinh.ecom.security;

import com.hqlinh.ecom.account.AccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String password = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new AccountException.PasswordNoMatchException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }
}
