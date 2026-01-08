package com.UU.UUPokedexSeptiembre2025Service.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String SECRET_KEY = "UUPokeDexApiSeptiembre2025112205";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String GenerateTokenUser(String userName, int user_Id, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_Id", user_Id);
        claims.put("rol", rol);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 + 60 + 60))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Claims GetAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims();
        }
    }

    public String GetUsernameFromToken(String token) {
        return GetAllClaims(token).getSubject();
    }

    public int GetUserIdFromToken(String token) {
        return (int) GetAllClaims(token).get("user_Id");
    }

    public String getRolFromToken(String token) {
        return (String) GetAllClaims(token).get("rol");
    }

    public boolean isExpired(String token) {

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());

        } catch (Exception ex) {
            return true;
        }

    }

    public String getJtiFromToken(String token) {
        return GetAllClaims(token).getId();
    }

}
