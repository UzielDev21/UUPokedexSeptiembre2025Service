package com.UU.UUPokedexSeptiembre2025Service.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OtherSpritesDTO(
        @JsonProperty("official-artwork") OfficialArtworkDTO officialArtwork
) {}