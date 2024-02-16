package com.hqlinh.ecom.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hqlinh.ecom.account.Account;
import com.hqlinh.ecom.account.IAccountRepository;
import com.hqlinh.ecom.auth.JWTService;
import com.hqlinh.ecom.core.APIResponse;
import com.hqlinh.ecom.core.ErrorResponse;
import com.hqlinh.ecom.util.ValueMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Collections;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final IAccountRepository accountRepository;
    private final JWTService jwtService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            String token = request.getHeader("Authorization");
            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                token = token.substring(7);
                String subject = jwtService.getSubjectToken(token);

                Account account = accountRepository.findById(Long.parseLong(subject)).orElseThrow(() -> new UsernameNotFoundException("Account not found!"));

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                String accessToken = jwtService.generateAccessToken(account);
                response.addHeader("Authorization", "Bearer " + accessToken);
            }
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException | UsernameNotFoundException ex) {
            APIResponse<String> res = APIResponse
                    .<String>builder()
                    .errors(Collections.singletonList(new ErrorResponse(null, ex.getMessage())))
                    .status("FAILED")
                    .build();
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(Objects.requireNonNull(ValueMapper.jsonAsString(res)));
            log.error("{}::JWTAuthenticationFilterException catch error: {}", ex.getClass().getSimpleName(), ValueMapper.jsonAsString(res));
        } catch (NumberFormatException ex) {
            log.error("{}::JWTAuthenticationFilterException catch error: token is not valid!", ex.getClass().getSimpleName());
            throw new NumberFormatException("token is not valid!");
        }
    }
}
