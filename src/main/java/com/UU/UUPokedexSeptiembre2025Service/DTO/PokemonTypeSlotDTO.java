package com.UU.UUPokedexSeptiembre2025Service.DTO;

// Sirve para recibir la información del tipo de un Pokémon de la API.

public record PokemonTypeSlotDTO(
        int slot,
        NamedApiResourceDTO type) {
}
