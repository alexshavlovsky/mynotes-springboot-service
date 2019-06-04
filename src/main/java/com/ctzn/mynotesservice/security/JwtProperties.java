package com.ctzn.mynotesservice.security;

class JwtProperties {
    static final String SECRET = "NobodyKnowsTheSecret";
    static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 10;
    static final String TOKEN_PREFIX = "Bearer ";
    static final String ROLES_CLAIM_KEY = "roles";
    static final String HEADER_STRING = "Authorization";
}
