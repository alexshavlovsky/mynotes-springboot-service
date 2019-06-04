package com.ctzn.mynotesservice.model.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserPasswordEncoder {
    private static PasswordEncoder instance = new BCryptPasswordEncoder();

    public static String encode(String rawPassword) {
        return instance.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return instance.matches(rawPassword, encodedPassword);
    }

    private UserPasswordEncoder() {
    }
}
