package com.UU.UUPokedexSeptiembre2025Service.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

// Sirve para agrupar las imágenes del Pokémon de la API.

public record PokemonImgDTO(
        @JsonProperty("front_default") String frontDefault,
        OtherImgDTO other
) {}
