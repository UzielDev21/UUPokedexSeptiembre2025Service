package com.UU.UUPokedexSeptiembre2025Service.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

// Contiene la URL de la imagen/ilustración oficial del Pokémon (artwork) de la API.

public record ArtOfficialDTO(
                @JsonProperty("front_default") String frontDefault) {
}
