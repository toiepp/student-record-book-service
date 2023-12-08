package com.lidiia.studentrecordbookservice.service;

import com.lidiia.studentrecordbookservice.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parserBuilder;

@Component
public class JwtService {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public void validateToken(final String token) {
        try {
            parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            System.out.println(token);
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            throw new RuntimeException("Token validation failed");
        }
    }

    public Authentication getAuthentication(String token) {
        try {
            Claims claims = parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();

            String username = claims.getSubject();
            UserRole role = UserRole.valueOf((String) claims.get("role"));

            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role.name()));

            UserDetails userDetails = new User(username, "", authorities);

            System.out.println("User '" + username + "' authenticated with role '" + role + "'");

            return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
        } catch (Exception e) {
            System.out.println("Error during authentication: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Authentication failed");
        }
    }


    public String generateToken(String email, UserRole role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
