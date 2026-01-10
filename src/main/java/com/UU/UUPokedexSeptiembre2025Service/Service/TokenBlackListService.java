package com.UU.UUPokedexSeptiembre2025Service.Service;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class TokenBlackListService implements ITokenInvalidationService {

    private final Set<String> listaNegra = ConcurrentHashMap.newKeySet();

    /**
     * Se elimino lo de redis
     * 
     */
    @Override
    public void invalidateToken(String jti) {
        listaNegra.add(jti);
        System.out.println("Token Invalidado Correctamente " + jti);
    }

    @Override
    public boolean isTokenInvalid(String jti) {
        return listaNegra.contains(jti);
    }
    
}
