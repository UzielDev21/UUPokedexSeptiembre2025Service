package com.UU.UUPokedexSeptiembre2025Service.DTO;

import java.util.List;

public record TypeDetailDTO(
        String name,
        List<TypePokemonSlotDTO> pokemon
) {}
