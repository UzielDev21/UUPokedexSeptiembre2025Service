package com.UU.UUPokedexSeptiembre2025Service.RestController;

import com.UU.UUPokedexSeptiembre2025Service.DTO.PokedexResponse;
import com.UU.UUPokedexSeptiembre2025Service.Service.PokedexService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para operaciones de Pokédex. Expone endpoints HTTP para
 * búsqueda, filtrado y listado de Pokémon.
 *
 * Ruta base: /api
 *
 * Responsabilidades: - Recibir solicitudes HTTP - Validar parámetros de entrada
 * - Delegar al servicio de negocio - Devolver respuestas formateadas con
 * códigos de estado HTTP
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
     * @param searchQuery Parámetro de búsqueda (opcional)
     * @param typeFilter Filtro por tipo (opcional)
     * @param limit Cantidad de resultados por página (default: 12)
     * @param offset Desplazamiento para paginación (default: 0)
     * @param sort Criterio de ordenamiento (default: "id_asc")
     * @return ResponseEntity con PokedexResponse (200 OK)
     */
    @GetMapping("/pokedex")
    public ResponseEntity<PokedexResponse> getPokedex(
            @RequestParam(name = "sear", required = false) String sear,
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "type", required = false) String typeFilter,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "id_asc") String sort
    ) {
        limit = Math.max(1, Math.min(48, limit));
        offset = Math.max(0, offset);

        String searQuery = "";
        if (id != null) {
            searQuery = String.valueOf(id);
        } else if (name != null && !name.trim().isBlank()) {
            searQuery = name.trim();
        } else if (sear != null && !sear.trim().isBlank()) {
            searQuery = sear.trim();
        }

        String TypeFilter = (typeFilter == null) ? "" : typeFilter.trim();

        PokedexResponse response = pokedexService.load(searQuery, TypeFilter, limit, offset, sort);
        return ResponseEntity.ok(response);
    }

}
