package com.UU.UUPokedexSeptiembre2025Service.DTO;

import java.util.List;

public record PokePageDTO(
        int count,
        String next,
        String previous,
        List<PokeRefDTO> results) {

}
