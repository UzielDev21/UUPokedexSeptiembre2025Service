package com.UU.UUPokedexSeptiembre2025Service.RestController;

import com.UU.UUPokedexSeptiembre2025Service.DTO.PokedexResponse;
import com.UU.UUPokedexSeptiembre2025Service.Service.PokedexService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para operaciones de Pokédex.
 * Expone endpoints HTTP para búsqueda, filtrado y listado de Pokémon.
 * 
 * Ruta base: /api
 * 
 * Responsabilidades:
 * - Recibir solicitudes HTTP
 * - Validar parámetros de entrada
 * - Delegar al servicio de negocio
 * - Devolver respuestas formateadas con códigos de estado HTTP
 */
@RestController
@RequestMapping("/api")
public class PokedexRestController {

    // Servicio que contiene la lógica de negocio
    private final PokedexService pokedexService;

    /**
     * Constructor que inyecta el servicio de Pokédex.
     * 
     * @param pokedexService Servicio de acceso a datos de Pokémon
     */
    public PokedexRestController(PokedexService pokedexService) {
        this.pokedexService = pokedexService;
    }

    /**
     * Obtiene lista de Pokémon con opciones de búsqueda, filtrado y paginación.
     * 
     * Endpoint: GET /api/pokedex
     * 
     * Parámetros soportados:
     * - sear: Búsqueda por nombre o ID (ej: "pikachu")
     * - type: Filtro por tipo (ej: "fire", "water")
     * - limit: Cantidad de resultados (default: 12, máximo: 48)
     * - offset: Desplazamiento para paginación (default: 0)
     * - sort: Ordenamiento (id_asc, id_desc, name_asc, name_desc)
     * 
     * Ejemplos:
     * - GET /api/pokedex?sear=pikachu
     * - GET /api/pokedex?type=fire&limit=20&sort=name_asc
     * - GET /api/pokedex?limit=30&offset=0
     * 
     * Las excepciones lanzadas son interceptadas por GlobalExceptionHandler
     * que mapea estados HTTP apropiados (400, 404, 500, etc).
     * 
     * @param searchQuery Parámetro de búsqueda (opcional)
     * @param typeFilter Filtro por tipo (opcional)
     * @param limit Cantidad de resultados por página (default: 12)
     * @param offset Desplazamiento para paginación (default: 0)
     * @param sort Criterio de ordenamiento (default: "id_asc")
     * @return ResponseEntity con PokedexResponse (200 OK)
     */
    @GetMapping("/pokedex")
    public ResponseEntity<PokedexResponse> getPokedex(
            @RequestParam(name = "sear", required = false) String searchQuery,
            @RequestParam(name = "type", required = false) String typeFilter,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "id_asc") String sort
    ) {
        // Validar y ajustar rangos de parámetros
        limit = Math.max(1, Math.min(48, limit));
        offset = Math.max(0, offset);
        
        // Limpiar parámetros de búsqueda
        searchQuery = (searchQuery == null) ? "" : searchQuery.trim();
        typeFilter = (typeFilter == null) ? "" : typeFilter.trim();

        // Delegar al servicio y devolver respuesta con 200 OK
        PokedexResponse response = pokedexService.load(searchQuery, typeFilter, limit, offset, sort);
        return ResponseEntity.ok(response);
    }
}
