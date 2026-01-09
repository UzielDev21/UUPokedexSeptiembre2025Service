package com.UU.UUPokedexSeptiembre2025Service.DTO;

// Sirve para la relación entre un Pokémon y su tipo con posición en la lista de un tipo de Pokémon de la API.

public record TypePokemonSlotDTO(
        NamedApiResourceDTO pokemon,
        int slot) {
}
