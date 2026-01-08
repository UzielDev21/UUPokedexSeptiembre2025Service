package com.UU.UUPokedexSeptiembre2025Service.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OfficialArtworkDTO(
        @JsonProperty("front_default") String frontDefault
) {}
