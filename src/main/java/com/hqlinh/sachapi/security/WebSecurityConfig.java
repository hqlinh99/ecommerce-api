package com.hqlinh.sachapi.security;

import com.hqlinh.sachapi.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
//    private final CustomOAuth2UserService customOAuth2UserService;

//    @Bean
//    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
//        return new HttpCookieOAuth2AuthorizationRequestRepository();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("Authorization"));

        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeRequests(authorize -> {
            authorize.requestMatchers("/oauth2/**").permitAll();
            authorize.requestMatchers(GET, "/upload/**", "/api/v1/products", "/api/v1/product/**").permitAll();
            authorize.requestMatchers(POST, "/api/v1/account", "/api/v1/login", "/api/v1/refresh-token").permitAll();
            authorize.anyRequest().authenticated();
        });
        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.oauth2Login(config -> {
            config.authorizationEndpoint(subconfig -> subconfig.baseUri("/oauth2/authorization"));
            config.redirectionEndpoint(subconfig -> subconfig.baseUri("/oauth2/callback/*"));
            config.successHandler(oAuth2AuthenticationSuccessHandler);
        });
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

