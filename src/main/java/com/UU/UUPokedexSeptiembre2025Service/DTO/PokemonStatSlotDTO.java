package com.UU.UUPokedexSeptiembre2025Service.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

// Sirve para recibir la información de las estadísticas de un Pokémon de la API.

public record PokemonStatSlotDTO(
        @JsonProperty("base_stat") int baseStat,
        NamedApiResourceDTO stat
) {}
