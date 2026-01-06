package com.UU.UUPokedexSeptiembre2025Service.Service;

public interface ITokenInvalidationService {

    void invalidateToken(String jti);
    
    boolean isTokenInvalid(String jti);
    
}
