package com.UU.UUPokedexSeptiembre2025Service.DTO;

import java.util.List;

// Sirve para enviar la información del Pokémon al cliente.

public record PokemonVista(
        int id,
        String name,
        List<String> types,
        int hp,
        int atk,
        int def,
        int spAtk,
        int spDef,
        int speed,
        String spriteUrl) {

}
