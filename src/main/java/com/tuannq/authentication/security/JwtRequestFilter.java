package com.tuannq.authentication.security;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.tuannq.authentication.config.DefaultVariable.JWT_TOKEN;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final MessageSource messageSource;
    private final UserDetailsService userDetailsService;

    private static final String HEADER = "Authorization";

    @Autowired
    public JwtRequestFilter(
            MessageSource messageSource,
            JwtTokenUtil jwtTokenUtil,
            UserDetailsService userDetailsService
    ) {
        this.messageSource = messageSource;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        log.info(httpServletRequest.getRemoteAddr());
//        System.out.println(httpServletRequest.getRequestURI());
        // Lấy token từ cookie
        String token;
        Cookie cookie = WebUtils.getCookie(httpServletRequest, JWT_TOKEN);
        if (cookie != null) {
            token = cookie.getValue();
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // Parse thông tin từ token
        Claims claims = jwtTokenUtil.getClaimsFromToken(token);
        if (claims == null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // Tạo object Authentication
        UsernamePasswordAuthenticationToken authenticationObject = getAuthentication(claims);
        if (authenticationObject == null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // Xác thực thành công, lưu object Authentication vào SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationObject);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(Claims claims) {
        String username = claims.getSubject();
        if (username == null) return null;
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (user == null) {
            log.info(messageSource.getMessage("email.not-found", null, LocaleContextHolder.getLocale()).concat(" ").concat(username));
            return null;
        }
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
}
