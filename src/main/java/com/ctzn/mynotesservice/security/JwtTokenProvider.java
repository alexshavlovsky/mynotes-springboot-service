package com.ctzn.mynotesservice.security;

import com.ctzn.mynotesservice.model.apimessage.TimeSource;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

import static com.ctzn.mynotesservice.model.user.UserRole.maskToAuthorities;

@Component
public class JwtTokenProvider {

    private final String secretKey = Base64.getEncoder().encodeToString(JwtProperties.SECRET.getBytes());
    private final JwtParser parser = Jwts.parser().setSigningKey(secretKey);

    public String createToken(String subject, int rolesMask) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put(JwtProperties.ROLES_CLAIM_KEY, rolesMask);
        Date now = TimeSource.now();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + JwtProperties.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    Authentication getAuthentication(HttpServletRequest req) {
        String header = req.getHeader(JwtProperties.HEADER_STRING);
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) return null;
        String token = header.substring(JwtProperties.TOKEN_PREFIX.length());
        try {
            Claims claims = parser.parseClaimsJws(token).getBody();
            String subject = claims.getSubject();
            Integer mask = claims.get(JwtProperties.ROLES_CLAIM_KEY, Integer.class);
            if (subject == null || mask == null) return null;
            return new UsernamePasswordAuthenticationToken(subject, null, maskToAuthorities(mask));
        } catch (Exception e) {
            return null;
        }
    }

}
