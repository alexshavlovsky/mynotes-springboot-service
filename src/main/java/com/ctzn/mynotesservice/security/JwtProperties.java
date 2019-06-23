package com.ctzn.mynotesservice.security;

class JwtProperties {
    static final boolean RANDOM_SECRET = false; // if false then use the fixed secret
    static final String FIXED_SECRET = "zHSFT4X1vWFWWF8jc5uy5RnQAYBISS_n3nOIYQMfeQM";
    static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 10;
    static final String TOKEN_PREFIX = "Bearer ";
    static final String ROLES_CLAIM_KEY = "roles";
    static final String HEADER_STRING = "Authorization";
}
