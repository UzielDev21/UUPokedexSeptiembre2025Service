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

    // HS256 requiere mínimo 32 bytes (256 bits). Tu clave tiene 32 chars, OK.
    private static final String SECRET_KEY = "UUPokeDexApiSeptiembre2025112205";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // 1 hora en milisegundos
    private static final long EXPIRATION_MS = 1000L * 60 * 60;

    public String GenerateTokenUser(String userName, int user_Id, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_Id", user_Id);
        claims.put("rol", rol);

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // además de firma válida, revisa expiración
            return claims.getExpiration() != null && claims.getExpiration().after(new Date());
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
        Object value = GetAllClaims(token).get("user_Id");
        if (value instanceof Number n) {
            return n.intValue();
        }
        return Integer.parseInt(String.valueOf(value));
    }

    public String getRolFromToken(String token) {
        return (String) GetAllClaims(token).get("rol");
    }

    public boolean isExpired(String token) {
        try {
            Claims claims = GetAllClaims(token);
            return claims.getExpiration() == null || claims.getExpiration().before(new Date());
        } catch (Exception ex) {
            return true;
        }
    }

    public String getJtiFromToken(String token) {
        return GetAllClaims(token).getId();
    }
}
