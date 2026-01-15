package com.UU.UUPokedexSeptiembre2025Service.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationTokenService {
    
    private final Map<String, Integer> verificationTokens = new ConcurrentHashMap<>();
    private final Map<String, Long> TokenExpirations = new ConcurrentHashMap<>();
    private final Set<String> usedTokens = ConcurrentHashMap.newKeySet();
    private static final long EXPIRATION_TIME = 5 * 60 * 1000;
    
    public String generateToken(int user_Id){
        
        String tokenEmail = UUID.randomUUID().toString();
        long expirationTime = System.currentTimeMillis() + EXPIRATION_TIME;
        
        verificationTokens.put(tokenEmail, user_Id);
        TokenExpirations.put(tokenEmail, expirationTime);

        return tokenEmail;
    }

    private void cleanupToken(String tokenEmail) {
        verificationTokens.remove(tokenEmail);
        TokenExpirations.remove(tokenEmail);
    }

    public Integer getUserIdFromToken(String tokenEmail) {
        return verificationTokens.get(tokenEmail);
    }

    public void markTokenAsUsed(String tokenEmail) {
        usedTokens.add(tokenEmail);
        cleanupToken(tokenEmail);
    }

    public boolean isTokenValid(String tokenEmail) {

        if (tokenEmail == null) {
            return false;
        }

        if (usedTokens.contains(tokenEmail)) {
            return false;
        }

        Long expiration = TokenExpirations.get(tokenEmail);
        if (expiration == null || expiration < System.currentTimeMillis()) {
            cleanupToken(tokenEmail);
            return false;
        }

        return verificationTokens.containsKey(tokenEmail);
        
    }
    
}
