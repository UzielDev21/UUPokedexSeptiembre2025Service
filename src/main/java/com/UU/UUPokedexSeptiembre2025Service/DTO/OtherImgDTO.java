package com.UU.UUPokedexSeptiembre2025Service.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

// Sirve para recibir otras imágenes del Pokémon de la API.

public record OtherImgDTO(
        @JsonProperty("official-artwork") ArtOfficialDTO officialArtwork
) {}