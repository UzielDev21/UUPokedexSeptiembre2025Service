package com.UU.UUPokedexSeptiembre2025Service.DTO;

import java.util.List;

// Sirve para recibir la información completa de un Pokémon de la API.

public record PokemonDetailDTO(
        int id,
        String name,
        List<PokemonTypeSlotDTO> types,
        List<PokemonStatSlotDTO> stats,
        PokemonImgDTO sprites
) {}
