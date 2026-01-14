package com.UU.UUPokedexSeptiembre2025Service.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenUsoService {

    private final Map<String, Integer> tokenUsos = new ConcurrentHashMap<>();
    private static final int LIMITE_USO = 35;

    public void registrarUso(String jti) {
        Integer ususActuales = tokenUsos.get(jti);

        if (ususActuales == null) {
            tokenUsos.put(jti, 1);
        } else {
            tokenUsos.put(jti, ususActuales + 1);
        }

        System.out.println("uso registrado: " + jti + " total usos: " + tokenUsos.get(jti));

    }

    public boolean excedioLimite(String jti) {
        return tokenUsos.getOrDefault(jti, 0) >= LIMITE_USO;
    }


}
