package com.tuannq.authentication.config;

import java.util.List;

public class DefaultVariable {
    // 7 ngày
    public static final int MAX_AGE_COOKIE = 7 * 24 * 60 * 60;

    public static final String JWT_TOKEN = "JWT_TOKEN";

    public static final List<String> IMAGE_EXTENSION = List.of("jpg", "png", "svg", "jpeg", "gif");

    public static final Integer LIMIT = 15;

}
