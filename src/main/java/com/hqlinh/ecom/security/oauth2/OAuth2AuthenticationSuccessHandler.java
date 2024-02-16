package com.hqlinh.ecom.security.oauth2;

import com.hqlinh.ecom.account.Account;
import com.hqlinh.ecom.account.IAccountRepository;
import com.hqlinh.ecom.account.Role;
import com.hqlinh.ecom.auth.JWTService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTService jwtService;
    private final IAccountRepository accountRepository;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private int expirationRefreshToken;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        Account tempAccount = new Account();
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("google")) {
            tempAccount.setAvatar(defaultOAuth2User.getAttributes().get("picture").toString());
            tempAccount.setFirstName(defaultOAuth2User.getAttributes().get("given_name").toString());
            tempAccount.setLastName(defaultOAuth2User.getAttributes().get("family_name").toString());
            tempAccount.setEmail(defaultOAuth2User.getAttributes().get("email").toString());
            tempAccount.setProviderId(defaultOAuth2User.getAttributes().get("sub").toString());
            tempAccount.setAuthProvider(AuthProvider.google);
            tempAccount.setRole(Role.CUSTOMER);
        } else if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("github")) {
            tempAccount.setAvatar(defaultOAuth2User.getAttributes().get("avatar_url").toString());
            tempAccount.setFirstName(defaultOAuth2User.getAttributes().get("name").toString());
            tempAccount.setProviderId(defaultOAuth2User.getAttributes().get("id").toString());
            tempAccount.setAuthProvider(AuthProvider.github);
            tempAccount.setRole(Role.CUSTOMER);
        }
        Account account = accountRepository.findByEmail(tempAccount.getEmail())
                .orElseGet(() -> accountRepository.findByProviderId(tempAccount.getProviderId())
                        .orElseGet(() -> accountRepository.save(tempAccount)));
        response.setContentType(MediaType.TEXT_HTML_VALUE);
        PrintWriter out = response.getWriter();
        Cookie cookie = new Cookie("access_token", jwtService.generateAccessToken(account));
        cookie.setMaxAge(1);
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie = new Cookie("refresh_token", jwtService.generateRefreshToken(account));
        cookie.setMaxAge(expirationRefreshToken / 1000);
        cookie.setPath("/");
        response.addCookie(cookie);
        out.println("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "<script>\n" +
                "    window.close();\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>");
        out.flush();
        out.close();
        response.setStatus(HttpStatus.OK.value());
    }
}