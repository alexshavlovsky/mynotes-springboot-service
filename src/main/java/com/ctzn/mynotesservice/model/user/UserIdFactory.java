package com.ctzn.mynotesservice.model.user;

import java.util.UUID;

public class UserIdFactory {
    private static String fixedUserId = UUID.randomUUID().toString();
    private static boolean fixed = false;

    public static void setFixed(boolean fixed) {
        UserIdFactory.fixed = fixed;
    }

    public static String produce() {
        return fixed ? fixedUserId : UUID.randomUUID().toString();
    }
}
