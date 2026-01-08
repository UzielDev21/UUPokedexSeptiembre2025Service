package com.UU.UUPokedexSeptiembre2025Service.DAO;

import com.UU.UUPokedexSeptiembre2025Service.DTO.PokePageDTO;
import com.UU.UUPokedexSeptiembre2025Service.DTO.PokemonDetailDTO;
import com.UU.UUPokedexSeptiembre2025Service.DTO.TypeDetailDTO;

/**
 * Contrato para acceso a datos de PokeAPI (API externa).
 * Define los métodos disponibles para obtener información de Pokémon.
 */
public interface IPokeApiRepository {

    /**
     * Obtiene una página paginada de la lista de Pokémon.
     * 
     * @param limit Cantidad de Pokémon por página
     * @param offset Desplazamiento desde el inicio
     * @return PokePageDTO con lista de Pokémon y metadatos
     */
    PokePageDTO getPokemonPage(int limit, int offset);

    /**
     * Obtiene los detalles completos de un Pokémon.
     * 
     * @param key Nombre o ID del Pokémon
     * @return PokemonDetailDTO con información completa
     */
    PokemonDetailDTO getPokemonDetail(String key);

    /**
     * Obtiene la información de un tipo de Pokémon y sus Pokémon asociados.
     * 
     * @param type Nombre del tipo (ej: "fire", "water")
     * @return TypeDetailDTO con información del tipo
     */
    TypeDetailDTO getTypeDetail(String type);
}