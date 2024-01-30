package com.hqlinh.sachapi.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hqlinh.sachapi.account.IAccountRepository;
import com.hqlinh.sachapi.auth.JWTService;
import com.hqlinh.sachapi.core.APIResponse;
import com.hqlinh.sachapi.core.ErrorResponse;
import com.hqlinh.sachapi.util.ValueMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final IAccountRepository accountRepository;
    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer")) {
                token = token.substring(7);
                String username = null;

                username = jwtService.getUsernameFromToken(token);

                UserDetails userDetails = accountRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                String accessToken = jwtService.generateAccessToken(userDetails);
                response.addHeader("Authorization", "Bearer " + accessToken);
            }
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException | JWTVerificationException ex) {
            APIResponse<String> res = APIResponse
                    .<String>builder()
                    .errors(Collections.singletonList(new ErrorResponse(null, ex.getMessage())))
                    .status("FAILED")
                    .build();
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(Objects.requireNonNull(ValueMapper.jsonAsString(res)));
            log.error("{}::handleJWTAuthenticationFilterException catch error: {}", ex.getClass().getSimpleName(), ValueMapper.jsonAsString(res));
        }
    }
}
