package com.hqlinh.sachapi.security;

import com.hqlinh.sachapi.core.APIResponse;
import com.hqlinh.sachapi.core.ErrorResponse;
import com.hqlinh.sachapi.util.ValueMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        APIResponse<String> res = APIResponse
                .<String>builder()
                .errors(Collections.singletonList(new ErrorResponse(null, accessDeniedException.getMessage())))
                .status("FAILED")
                .build();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(Objects.requireNonNull(ValueMapper.jsonAsString(res)));
    }
}
