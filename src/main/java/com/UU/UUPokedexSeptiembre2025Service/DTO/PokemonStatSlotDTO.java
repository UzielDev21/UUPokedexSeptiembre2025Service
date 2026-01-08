package com.UU.UUPokedexSeptiembre2025Service.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.lang.model.element.Name;

public record PokemonStatSlotDTO(
        @JsonProperty("base_stat") int baseStat,
        NamedApiResourceDTO stat
) {}
