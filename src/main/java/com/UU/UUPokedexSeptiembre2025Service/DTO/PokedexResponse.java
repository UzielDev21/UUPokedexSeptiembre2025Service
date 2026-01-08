package com.UU.UUPokedexSeptiembre2025Service.DTO;

import java.util.List;
import java.util.List;

public record PokedexResponse(
        List<PokemonVm> pokes,
        Integer count,
        boolean hasNext,
        boolean hasPrev
) {}