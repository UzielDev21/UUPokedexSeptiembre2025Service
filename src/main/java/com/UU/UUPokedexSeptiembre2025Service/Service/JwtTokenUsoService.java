package com.UU.UUPokedexSeptiembre2025Service.Service;

import java.time.Duration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenUsoService {

    private static final String PREFIX_TOKEN_USO = "jwt:usage:";
    private static final int LIMITE_USO = 35;

    private final StringRedisTemplate redisTemplate;

    public JwtTokenUsoService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void registrarUso(String jti) {
        String key = PREFIX_TOKEN_USO + jti;

        Long usosActuales = redisTemplate.opsForValue().increment(key);

        if (usosActuales != null && usosActuales == 1) {
            redisTemplate.expire(key, Duration.ofHours(2));
        }
        
        System.out.println("Uso registrado en Redis, jti:" + jti + " | Total de usos: " + usosActuales);   
    }

    public boolean excedioLimite(String jti){
        String key = PREFIX_TOKEN_USO + jti;
        String valor = redisTemplate.opsForValue().get(key);
        
        if (valor == null) {
            return false;
        }
        
        int usos = Integer.parseInt(valor);
        return usos >= LIMITE_USO;
    }
    
}
