package com.UU.UUPokedexSeptiembre2025Service.Configuration;

import com.UU.UUPokedexSeptiembre2025Service.DTO.PokePageDTO;
import com.UU.UUPokedexSeptiembre2025Service.DTO.PokemonDetailDTO;
import com.UU.UUPokedexSeptiembre2025Service.DTO.TypeDetailDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class PokeApi {

    private static final String BASE = "https://pokeapi.co/api/v2";

    private final RestClient http = RestClient.builder()
            .baseUrl(BASE)
            .build();

    public PokePageDTO getPokemonPage(int limit, int offset) {
        try {
            PokePageDTO body = http.get()
                    .uri("/pokemon?limit={limit}&offset={offset}", limit, offset)
                    .retrieve()
                    .body(PokePageDTO.class);

            if (body == null) {
                throw new IllegalStateException("PokeAPI devolvió null en /pokemon");
            }
            return body;

        } catch (RestClientException ex) {
            throw new RuntimeException("Error llamando PokeAPI /pokemon: " + ex.getMessage(), ex);
        }
    }

    public PokemonDetailDTO getPokemonDetail(String key) {
        try {
            PokemonDetailDTO body = http.get()
                    .uri("/pokemon/{key}", key.toLowerCase())
                    .retrieve()
                    .body(PokemonDetailDTO.class);

            if (body == null) {
                throw new IllegalStateException("PokeAPI devolvió null en /pokemon/" + key);
            }
            return body;

        } catch (RestClientException ex) {
            throw new RuntimeException("Error llamando PokeAPI /pokemon/" + key + ": " + ex.getMessage(), ex);
        }
    }

    public TypeDetailDTO getTypeDetail(String type) {
        try {
            TypeDetailDTO body = http.get()
                    .uri("/type/{type}", type.toLowerCase())
                    .retrieve()
                    .body(TypeDetailDTO.class);

            if (body == null) {
                throw new IllegalStateException("PokeAPI devolvió null en /type/" + type);
            }
            return body;

        } catch (RestClientException ex) {
            throw new RuntimeException("Error llamando PokeAPI /type/" + type + ": " + ex.getMessage(), ex);
        }
    }
}
