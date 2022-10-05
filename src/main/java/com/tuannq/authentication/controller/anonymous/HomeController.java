package com.tuannq.authentication.controller.anonymous;

import com.tuannq.authentication.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final AuthUtils authUtils;

    @GetMapping("/")
    public String account(
            HttpServletResponse httpResponse
    ) throws IOException {
        var userOpt = authUtils.getUser();
        if (userOpt.isEmpty()) {
            httpResponse.sendRedirect("/login");
            return null;
        }

        httpResponse.sendRedirect("/survey");
        return null;
    }
}
