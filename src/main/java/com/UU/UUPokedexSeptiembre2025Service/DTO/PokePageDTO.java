package com.UU.UUPokedexSeptiembre2025Service.DTO;

import java.util.List;

// Sirve para recibir la página de Pokémones de la API.

public record PokePageDTO(
        int count,
        String next,
        String previous,
        List<PokeRefDTO> results) {

}
