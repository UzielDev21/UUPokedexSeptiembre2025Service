package com.UU.UUPokedexSeptiembre2025Service.Service;

import java.time.Duration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenBlackListService implements ITokenInvalidationService {

    private static final String PREFIX_BLACKLIST = "jwt:blacklist:";
    private static final String VALUE_INVALID = "INVALIDO";

    private final StringRedisTemplate redisTemplate;

    public TokenBlackListService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Invalida el token usando el jti del token
     * -------------------------------------- Se hace el almacenamiento en Redis
     * con el TTL especificado --------------------------------------
     */
    @Override
    public void invalidateToken(String jti) {
        if (jti == null || jti.isBlank()) {
            return;
        }

        String key = PREFIX_BLACKLIST + jti;

        redisTemplate.opsForValue().set(key, VALUE_INVALID);
        redisTemplate.expire(key, Duration.ofHours(2));

        System.out.println("El token se invalido en Redis: " + jti);
    }

    @Override
    public boolean isTokenInvalid(String jti) {
        if (jti == null || jti.isBlank()) {
            return false;
        }

        String key = PREFIX_BLACKLIST + jti;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
