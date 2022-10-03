package com.tuannq.authentication.security;

import com.google.gson.Gson;
import com.tuannq.authentication.model.dto.UserDTO;
import com.tuannq.authentication.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;

import static com.tuannq.authentication.config.DefaultVariable.MAX_AGE_COOKIE;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtResponseFilter extends OncePerRequestFilter {
    private final AuthUtils authUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain
    ) throws ServletException, IOException {

        var userOpt = authUtils.getUser();
        var requestURI = httpServletRequest.getRequestURI();
        if (userOpt.isEmpty() || requestURI.contains("/logout") || requestURI.contains("/login")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        var user = userOpt.get();
        var gson = new Gson();

        // Add token to cookie
        Cookie cookie = new Cookie("info", URLEncoder.encode(gson.toJson(new UserDTO(user)), StandardCharsets.UTF_8));
        cookie.setMaxAge(MAX_AGE_COOKIE);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}