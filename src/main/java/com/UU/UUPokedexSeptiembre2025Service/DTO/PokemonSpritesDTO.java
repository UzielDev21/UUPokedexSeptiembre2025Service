package com.UU.UUPokedexSeptiembre2025Service.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PokemonSpritesDTO(
        @JsonProperty("front_default") String frontDefault,
        OtherSpritesDTO other
) {}
