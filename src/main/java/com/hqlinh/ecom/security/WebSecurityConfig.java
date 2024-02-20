package com.hqlinh.ecom.security;

import com.hqlinh.ecom.security.oauth2.OAuth2AuthenticationFailedHandler;
import com.hqlinh.ecom.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.hqlinh.ecom.account.Role.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailedHandler oAuth2AuthenticationFailedHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeRequests(authorize -> {
            authorize.requestMatchers("/oauth2/**", "/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll();
            authorize.requestMatchers(GET, "/upload/**", "/api/v1/products", "/api/v1/product/**", "/api/v1/vnpay-callback").permitAll();
            authorize.requestMatchers(POST, "/api/v1/account", "/api/v1/login", "/api/v1/refresh-token").permitAll();

            authorize.requestMatchers(GET, "/api/v1/accounts").hasAnyRole(ADMIN.name());

            authorize.requestMatchers(POST, "/api/v1/product").hasAnyRole(ADMIN.name(), MANAGER.name(), SUB_MANAGER.name());
            authorize.requestMatchers(PATCH, "/api/v1/product/**").hasAnyRole(ADMIN.name(), MANAGER.name(), SUB_MANAGER.name());
            authorize.requestMatchers(DELETE, "/api/v1/product/**").hasAnyRole(ADMIN.name(), MANAGER.name());

            authorize.anyRequest().authenticated();
        });
        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.oauth2Login(config -> {
            config.authorizationEndpoint(subconfig -> subconfig.baseUri("/oauth2/authorization"));
            config.redirectionEndpoint(subconfig -> subconfig.baseUri("/oauth2/callback/*"));
            config.successHandler(oAuth2AuthenticationSuccessHandler);
            config.failureHandler(oAuth2AuthenticationFailedHandler);
            config.loginPage("/error");
        });
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(exception -> {
            exception.accessDeniedHandler(customAccessDeniedHandler);
        });
        return http.build();
    }
}

