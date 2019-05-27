package com.ctzn.mynotesservice.model.apimessage;

import java.util.Date;

public class TimestampSource {
    private static Date fixedTimestamp = new Date();
    private static boolean isFixed = false;

    public static void setFixed(boolean fixed) {
        isFixed = fixed;
    }

    static Date getTimestamp() {
        return isFixed ? fixedTimestamp : new Date();
    }
}
