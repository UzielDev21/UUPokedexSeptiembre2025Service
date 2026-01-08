package com.UU.UUPokedexSeptiembre2025Service.DTO;

import java.util.List;

public record PokemonVm(
        int id,
        String name,
        List<String> types,
        Integer hp,
        Integer atk,
        Integer def,
        Integer spAtk,
        Integer spDef,
        Integer speed,
        String spriteUrl) {

}
