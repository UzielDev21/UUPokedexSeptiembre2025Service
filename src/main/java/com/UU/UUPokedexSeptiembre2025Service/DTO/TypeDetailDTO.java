package com.UU.UUPokedexSeptiembre2025Service.DTO;

import java.util.List;

// Sirve para recibir la información de un tipo de Pokémon de la API.

public record TypeDetailDTO(
        String name,
        List<TypePokemonSlotDTO> pokemon
) {}
