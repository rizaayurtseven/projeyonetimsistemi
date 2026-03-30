package com.stajprojesi.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Collections;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration:36000000}")
    private long expiration; // varsayılan 10 saat

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Yeni generateToken metodu: Rolleri de alacak
    public String generateToken(String username, Set<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles); // Rolleri claims'e ekle
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // JWT'den rolleri çıkarmak için yeni metod (isteğe bağlı, kullanılabilir)
    public Set<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        // "roles" claim'ini doğru tipte (List.class veya Collection.class) çıkarmak önemli
        List<?> rolesList = claims.get("roles", List.class);
        if (rolesList != null) {
            return rolesList.stream()
                             .map(Object::toString)
                             .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}