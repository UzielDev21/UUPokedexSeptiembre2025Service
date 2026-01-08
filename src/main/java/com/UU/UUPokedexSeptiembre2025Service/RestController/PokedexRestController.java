package com.UU.UUPokedexSeptiembre2025Service.RestController;

import com.UU.UUPokedexSeptiembre2025Service.DTO.PokedexResponse;
import com.UU.UUPokedexSeptiembre2025Service.Service.PokedexService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PokedexRestController {

    private final PokedexService pokedexService;

    public PokedexRestController(PokedexService pokedexService) {
        this.pokedexService = pokedexService;
    }

    @GetMapping("/pokedex")
    public PokedexResponse getPokedex(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "id_asc") String sort
    ) {
        limit = Math.max(1, Math.min(48, limit));
        offset = Math.max(0, offset);

        return pokedexService.load(q, type, limit, offset, sort);
    }
}
