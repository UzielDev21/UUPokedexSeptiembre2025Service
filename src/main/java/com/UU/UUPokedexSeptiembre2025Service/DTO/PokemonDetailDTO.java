package com.UU.UUPokedexSeptiembre2025Service.DTO;

import java.util.List;

public record PokemonDetailDTO(
        int id,
        String name,
        List<PokemonTypeSlotDTO> types,
        List<PokemonStatSlotDTO> stats,
        PokemonSpritesDTO sprites
) {}
