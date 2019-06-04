package com.ctzn.mynotesservice.security;

import com.ctzn.mynotesservice.model.apimessage.TimeSource;
import com.ctzn.mynotesservice.model.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey = Base64.getEncoder().encodeToString(JwtProperties.SECRET.getBytes());

    public String createToken(String subject, int roleBitmask) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put(JwtProperties.ROLES_CLAIM_KEY, roleBitmask);

        Date now = TimeSource.now();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + JwtProperties.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    String getToken(HttpServletRequest req) {
        String header = req.getHeader(JwtProperties.HEADER_STRING);
        return (header != null && header.startsWith(JwtProperties.TOKEN_PREFIX)) ?
                header.substring(JwtProperties.TOKEN_PREFIX.length()) : null;
    }

    boolean validateToken(String token) {
        return !Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration().before(TimeSource.now());
    }

    private String getSubject(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    private <T> T getClaimByKey(String token, String key, Class<T> type) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get(key, type);
    }

    Authentication getAuthentication(String token) {
        int roles = getClaimByKey(token, JwtProperties.ROLES_CLAIM_KEY, Integer.class);
        return new UsernamePasswordAuthenticationToken(getSubject(token), null, UserRole.maskToAuthorities(roles));
    }

}
