package com.UU.UUPokedexSeptiembre2025Service.DTO;

/**
 * DTO para respuestas de error estándar en la API.
 * Contiene información básica para diagnóstico y mapeo HTTP.
 */
public record ErrorResponse(
        int status,
        String error,
        String message,
        long timestamp,
        String path
) {
}
