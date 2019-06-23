package com.ctzn.mynotesservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    // this filter extracts JWT an authentication token from the request header
    // which must contain public user id and granted user roles
    // user public id and granted authorities are then stored in the SecurityContext
    // if token is expired or any error happens, no authentication information will be stored
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication auth = jwtTokenProvider.getAuthentication(request);
        if (auth != null) SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

}
