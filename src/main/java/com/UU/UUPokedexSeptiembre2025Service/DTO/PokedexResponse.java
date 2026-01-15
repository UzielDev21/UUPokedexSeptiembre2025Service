package com.UU.UUPokedexSeptiembre2025Service.DTO;

import java.util.List;

// Sirve para recibir la respuesta de la API al pedir la lista de Pokémones.

public record PokedexResponse(
        List<PokemonVista> pokes,
        Integer count,
        boolean hasNext,
        boolean hasPrev
) {}
