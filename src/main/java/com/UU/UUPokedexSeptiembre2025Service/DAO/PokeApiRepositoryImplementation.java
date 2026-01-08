package com.UU.UUPokedexSeptiembre2025Service.DAO;

import com.UU.UUPokedexSeptiembre2025Service.DTO.PokePageDTO;
import com.UU.UUPokedexSeptiembre2025Service.DTO.PokemonDetailDTO;
import com.UU.UUPokedexSeptiembre2025Service.DTO.TypeDetailDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Implementación del repositorio PokeAPI.
 * Realiza las solicitudes HTTP a la API externa pokeapi.co.
 * 
 * Responsabilidades:
 * - Comunicación HTTP con PokeAPI
 * - Deserialización de respuestas JSON
 * - Manejo de errores y validaciones
 */
@Component
public class PokeApiRepositoryImplementation implements IPokeApiRepository {

    // URL base de PokeAPI
    private static final String BASE_URL = "https://pokeapi.co/api/v2";

    // Cliente HTTP para realizar solicitudes
    private final RestClient httpClient = RestClient.builder()
            .baseUrl(BASE_URL)
            .build();

    /**
     * Obtiene una página paginada de la lista de Pokémon.
     * Endpoint: GET /pokemon?limit={limit}&offset={offset}
     * 
     * @param limit Cantidad de Pokémon por página
     * @param offset Desplazamiento desde el inicio
     * @return PokePageDTO con lista de Pokémon y metadatos de paginación
     * @throws RuntimeException si hay error en la solicitud HTTP
     */
    @Override
    public PokePageDTO getPokemonPage(int limit, int offset) {
        try {
            // Realizar solicitud GET a /pokemon con parámetros limit y offset
            PokePageDTO response = httpClient.get()
                    .uri("/pokemon?limit={limit}&offset={offset}", limit, offset)
                    .retrieve()
                    .body(PokePageDTO.class);

            // Validar que la respuesta no sea nula
            if (response == null) {
                throw new IllegalStateException("PokeAPI devolvió null en endpoint /pokemon");
            }
            
            return response;

        } catch (RestClientException httpError) {
            // Capturar errores HTTP y relanzar con contexto
            throw new RuntimeException("Error al obtener página de Pokémon de PokeAPI: " + httpError.getMessage(), httpError);
        }
    }

    /**
     * Obtiene los detalles completos de un Pokémon específico.
     * Endpoint: GET /pokemon/{key}
     * 
     * @param key Nombre o ID del Pokémon (ej: "pikachu", "25")
     * @return PokemonDetailDTO con información completa (tipos, stats, sprites, etc)
     * @throws RuntimeException si hay error en la solicitud HTTP
     */
    @Override
    public PokemonDetailDTO getPokemonDetail(String key) {
        try {
            // Realizar solicitud GET a /pokemon/{key}
            PokemonDetailDTO response = httpClient.get()
                    .uri("/pokemon/{key}", key.toLowerCase())
                    .retrieve()
                    .body(PokemonDetailDTO.class);

            // Validar que la respuesta no sea nula
            if (response == null) {
                throw new IllegalStateException("PokeAPI devolvió null en endpoint /pokemon/" + key);
            }
            
            return response;

        } catch (RestClientException httpError) {
            // Capturar errores HTTP y relanzar con contexto
            throw new RuntimeException("Error al obtener detalles de Pokémon '" + key + "' de PokeAPI: " + httpError.getMessage(), httpError);
        }
    }

    /**
     * Obtiene la información de un tipo de Pokémon y todos los Pokémon de ese tipo.
     * Endpoint: GET /type/{type}
     * 
     * @param type Nombre del tipo (ej: "fire", "water", "electric")
     * @return TypeDetailDTO con información del tipo y lista de Pokémon
     * @throws RuntimeException si hay error en la solicitud HTTP
     */
    @Override
    public TypeDetailDTO getTypeDetail(String type) {
        try {
            // Realizar solicitud GET a /type/{type}
            TypeDetailDTO response = httpClient.get()
                    .uri("/type/{type}", type.toLowerCase())
                    .retrieve()
                    .body(TypeDetailDTO.class);

            // Validar que la respuesta no sea nula
            if (response == null) {
                throw new IllegalStateException("PokeAPI devolvió null en endpoint /type/" + type);
            }
            
            return response;

        } catch (RestClientException httpError) {
            // Capturar errores HTTP y relanzar con contexto
            throw new RuntimeException("Error al obtener tipo de Pokémon '" + type + "' de PokeAPI: " + httpError.getMessage(), httpError);
        }
    }
}