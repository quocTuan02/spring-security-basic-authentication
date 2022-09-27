package com.tuannq.authentication.security;

import com.tuannq.authentication.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@RequiredArgsConstructor
public class ConfigInterceptor implements HandlerInterceptor {
    private final AuthUtils authUtils;

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView
    ) throws Exception {
        if (modelAndView == null) return;

        var userOpt = authUtils.getUser();
        userOpt.ifPresent(users -> modelAndView.addObject("_user", users));
    }
}
