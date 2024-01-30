package com.hqlinh.sachapi.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.util.Date;
import java.util.stream.Collectors;


@Service
public class JWTService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.access-token.expiration}")
    private long expirationAccessToken;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long expirationRefreshToken;

    public String generateAccessToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationAccessToken))
                .withClaim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(Algorithm.HMAC256(secretKey.getBytes()));
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationRefreshToken))
                .sign(Algorithm.HMAC256(secretKey.getBytes()));
    }

    public String getUsernameFromToken(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(secretKey.getBytes())).build().verify(token).getSubject();
    }
}
