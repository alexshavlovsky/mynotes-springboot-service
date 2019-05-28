package com.ctzn.mynotesservice.model.apimessage;

import java.util.Date;

public class TimeSource {
    private static Date fixedTimestamp = new Date();
    private static boolean fixed = false;

    public static void setFixed(boolean fixed) {
        TimeSource.fixed = fixed;
    }

    public static Date now() {
        return fixed ? fixedTimestamp : new Date();
    }
}
