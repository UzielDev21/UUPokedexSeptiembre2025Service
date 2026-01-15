package com.UU.UUPokedexSeptiembre2025Service.Service;

import com.UU.UUPokedexSeptiembre2025Service.DAO.IPokeApiRepository;
import com.UU.UUPokedexSeptiembre2025Service.DTO.PokePageDTO;
import com.UU.UUPokedexSeptiembre2025Service.DTO.PokeRefDTO;
import com.UU.UUPokedexSeptiembre2025Service.DTO.PokedexResponse;
import com.UU.UUPokedexSeptiembre2025Service.DTO.PokemonDetailDTO;
import com.UU.UUPokedexSeptiembre2025Service.DTO.PokemonStatSlotDTO;
import com.UU.UUPokedexSeptiembre2025Service.DTO.PokemonVista;
import com.UU.UUPokedexSeptiembre2025Service.DTO.TypeDetailDTO;
import com.UU.UUPokedexSeptiembre2025Service.Util.SynchronizedLruCache;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * Servicio de gestión de datos de Pokémon. Maneja consultas, caching en memoria
 * y transformación de DTOs. Utiliza concurrencia controlada para optimizar
 * solicitudes a la API.
 */
@Service
public class PokedexService {

    private final IPokeApiRepository api;
    private final ExecutorService executor;

    // === CACHES: Almacenamiento en memoria con evicción LRU ===
    // detailsCache: Almacena detalles completos de Pokémon (300 máximo)
    private final SynchronizedLruCache<String, PokemonDetailDTO> detailsCache = new SynchronizedLruCache<>(300);
    // typeCache: Almacena tipos de Pokémon y sus relaciones (80 máximo)
    private final SynchronizedLruCache<String, TypeDetailDTO> typeCache = new SynchronizedLruCache<>(80);
    // pageCache: Almacena páginas paginadas de búsqueda/filtrado (80 máximo)
    private final SynchronizedLruCache<String, PokedexResponse> pageCache = new SynchronizedLruCache<>(80);

    /**
     * Constructor que inyecta la configuración de API y el ejecutor de tareas
     * concurrentes.
     *
     * @param api Cliente para comunicación con PokeAPI
     * @param pokeApiExecutor Pool de threads para solicitudes paralelas
     */
    public PokedexService(IPokeApiRepository api, ExecutorService pokeApiExecutor) {
        this.api = api;
        this.executor = pokeApiExecutor;
    }

    /**
     * Método principal que orquesta la carga de datos. Soporta: búsqueda por
     * nombre/ID, filtrado por tipo, o listado paginado.
     *
     * @param searchQuery Parámetro de búsqueda (nombre o ID de Pokémon)
     * @param typeFilter Filtro por tipo de Pokémon
     * @param limit Cantidad de resultados por página
     * @param offset Desplazamiento para paginación
     * @param sort Criterio de ordenamiento (id_asc, id_desc, name_asc,
     * name_desc)
     * @return PokedexResponse con Pokémon transformados y metadatos de
     * paginación
     */
    public PokedexResponse load(String searchQuery, String typeFilter, int limit, int offset, String sort) {
        // Normalizar parámetros de entrada
        searchQuery = (searchQuery == null ? "" : searchQuery.trim().toLowerCase());
        typeFilter = (typeFilter == null ? "" : typeFilter.trim().toLowerCase());
        sort = (sort == null ? "id_asc" : sort.trim().toLowerCase());

        // Caso 1: Búsqueda específica por nombre o ID
        if (!searchQuery.isBlank()) {
            PokemonDetailDTO pokemonDetail = getPokemonDetailCached(searchQuery);
            return new PokedexResponse(sortPokes(List.of(toPokemonVista(pokemonDetail)), sort), 1, false, false);
        }

        // Caso 2: Filtrado por tipo de Pokémon
        if (!typeFilter.isBlank()) {
            return loadType(typeFilter, limit, offset, sort);
        }

        // Caso 3: Listado general paginado
        return loadList(limit, offset, sort);
    }

    /**
     * Carga una página del listado general de Pokémon. Realiza solicitudes
     * paralelas (máx. 8 concurrentes) para obtener detalles. Resultado se
     * cachea con clave compuesta incluida parámetros.
     *
     * @param limit Cantidad de registros por página
     * @param offset Desplazamiento desde el inicio
     * @param sort Criterio de ordenamiento
     * @return PokedexResponse con página de Pokémon y metadatos
     */
    private PokedexResponse loadList(int limit, int offset, String sort) {
        // Generar clave única para caché basada en parámetros
        String cacheKey = "list|limit=" + limit + "|offset=" + offset + "|sort=" + sort;
        if (pageCache.containsKey(cacheKey)) {
            return pageCache.get(cacheKey);
        }

        // Obtener página de referencias de la API
        PokePageDTO pageData = api.getPokemonPage(limit, offset);
        List<String> pokemonNames = pageData.results() == null ? List.of()
                : pageData.results().stream().map(PokeRefDTO::name).toList();

        // Obtener detalles con concurrencia controlada (máx 8 hilos paralelos)
        List<PokemonDetailDTO> detailedPokemons = mapLimit(pokemonNames, 8, this::getPokemonDetailCached);
        List<PokemonVista> pokemonViews = sortPokes(detailedPokemons.stream().map(this::toPokemonVista).toList(), sort);

        // Armar respuesta con metadatos de paginación
        PokedexResponse response = new PokedexResponse(
                pokemonViews,
                pageData.count(),
                pageData.next() != null && !pageData.next().isBlank(),
                pageData.previous() != null && !pageData.previous().isBlank()
        );

        // Guardar en caché y devolver
        pageCache.put(cacheKey, response);
        return response;
    }

    private PokedexResponse loadType(String typeFilter, int limit, int offset, String sort) {
        String cacheKey = "type|" + typeFilter + "|limit=" + limit + "|offset=" + offset + "|sort=" + sort;
        if (pageCache.containsKey(cacheKey)) {
            return pageCache.get(cacheKey);
        }

        // Obtener información completa del tipo (incluye lista de Pokémon)
        TypeDetailDTO typeDetail = getTypeDetailCached(typeFilter);

        // Extraer nombres de Pokémon del tipo
        List<String> allPokemonInType = typeDetail.pokemon() == null ? List.of()
                : typeDetail.pokemon().stream()
                        .map(typePokemonSlot -> typePokemonSlot.pokemon() != null ? typePokemonSlot.pokemon().name() : null)
                        .filter(Objects::nonNull)
                        .toList();

        // Calcular rangos de paginación
        int totalCount = allPokemonInType.size();
        int startIndex = Math.min(offset, totalCount);
        int endIndex = Math.min(offset + limit, totalCount);

        // Obtener slice de la lista según paginación
        List<String> paginatedNames = allPokemonInType.subList(startIndex, endIndex);

        // Realizar solicitudes paralelas para obtener detalles de Pokémon paginados
        List<PokemonDetailDTO> detailedPokemons = mapLimit(paginatedNames, 8, this::getPokemonDetailCached);
        List<PokemonVista> pokemonViews = sortPokes(detailedPokemons.stream().map(this::toPokemonVista).toList(), sort);

        // Construir respuesta con metadatos de paginación
        PokedexResponse res = new PokedexResponse(
                pokemonViews,
                totalCount,
                offset + limit < totalCount,
                offset > 0
        );

        pageCache.put(cacheKey, res);
        return res;
    }

    /**
     * Ejecuta funciones de forma paralela con límite de concurrencia. Utiliza
     * Semaphore para controlar el máximo de threads activos simultáneamente.
     *
     * @param <T> Tipo de elementos de entrada
     * @param <R> Tipo de elementos de salida transformados
     * @param items Lista de elementos a procesar
     * @param maxConcurrent Máximo de operaciones concurrentes (límite de
     * semáforo)
     * @param fn Función que transforma cada elemento
     * @return Lista de resultados en el mismo orden que la entrada
     */
    private <T, R> List<R> mapLimit(List<T> items, int maxConcurrent, Function<T, R> fn) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }

        // Semáforo que limita cuántos threads pueden ejecutarse simultáneamente
        Semaphore concurrencyLimiter = new Semaphore(Math.max(1, maxConcurrent));

        // Crear tareas asincrónicas para cada elemento
        List<CompletableFuture<R>> futures = items.stream()
                .map(item -> CompletableFuture.supplyAsync(() -> {
            try {
                // Adquirir permiso del semáforo (bloquea si se alcanza el límite)
                concurrencyLimiter.acquire();
                return fn.apply(item);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } finally {
                // Liberar permiso siempre (permitir siguientes threads)
                concurrencyLimiter.release();
            }
        }, executor))
                .toList();

        // Esperar a que todas las tareas se completen
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Recopilar resultados manteniendo el orden original
        List<R> results = new ArrayList<>(items.size());
        for (CompletableFuture<R> future : futures) {
            results.add(future.join());
        }
        return results;
    }

    /**
     * Obtiene detalles completos de un Pokémon desde caché o API. Implementa
     * patrón de doble verificación para eficiencia.
     *
     * @param pokemonIdentifier Nombre o ID del Pokémon
     * @return PokemonDetailDTO con información completa
     */
    private PokemonDetailDTO getPokemonDetailCached(String pokemonIdentifier) {
        String normalizedKey = pokemonIdentifier.toLowerCase();
        if (detailsCache.containsKey(normalizedKey)) {
            return detailsCache.get(normalizedKey);
        }

        PokemonDetailDTO pokemonDetail = api.getPokemonDetail(normalizedKey);
        detailsCache.put(normalizedKey, pokemonDetail);
        return pokemonDetail;
    }

    /**
     * Obtiene información detallada de un tipo de Pokémon desde caché o API.
     * Incluye lista de todos los Pokémon que pertenecen a ese tipo.
     *
     * @param typeIdentifier Nombre del tipo (ej: "fire", "water")
     * @return TypeDetailDTO con información del tipo y sus Pokémon
     */
    private TypeDetailDTO getTypeDetailCached(String typeIdentifier) {
        String normalizedKey = typeIdentifier.toLowerCase();
        if (typeCache.containsKey(normalizedKey)) {
            return typeCache.get(normalizedKey);
        }

        TypeDetailDTO typeDetail = api.getTypeDetail(normalizedKey);
        typeCache.put(normalizedKey, typeDetail);
        return typeDetail;
    }

    /**
     * Transforma un PokemonDetailDTO a PokemonVista (vista para cliente).
     * Extrae tipos, estadísticas y resuelve URLs de sprite con fallback.
     *
     * @param pokemonDetail DTO con información completa del Pokémon
     * @return PokemonVista objeto de transferencia para enviar al cliente
     */
    private PokemonVista toPokemonVista(PokemonDetailDTO pokemonDetail) {
        int pokemonId = pokemonDetail.id();
        String pokemonName = pokemonDetail.name();

        // Extraer nombres de tipos del Pokémon
        List<String> pokemonTypes = pokemonDetail.types() == null ? List.of()
                : pokemonDetail.types().stream()
                        .map(pokemonTypeSlot -> pokemonTypeSlot.type() != null ? pokemonTypeSlot.type().name() : null)
                        .filter(Objects::nonNull)
                        .toList();

        // Construir mapa de estadísticas (nombre -> valor)
        Map<String, Integer> statsByName = new HashMap<>();
        if (pokemonDetail.stats() != null) {
            for (PokemonStatSlotDTO statSlot : pokemonDetail.stats()) {
                if (statSlot.stat() != null && statSlot.stat().name() != null) {
                    statsByName.put(statSlot.stat().name(), statSlot.baseStat());
                }
            }
        }

        // Resolver URL de sprite con prioridad: officialArtwork > frontDefault > fallback por ID
        String spriteUrl = resolveSpriteUrl(pokemonDetail, pokemonId);

        // Construir y retornar DTO de respuesta
        return new PokemonVista(
                pokemonId,
                pokemonName,
                pokemonTypes,
                statsByName.getOrDefault("hp", 0),
                statsByName.getOrDefault("attack", 0),
                statsByName.getOrDefault("defense", 0),
                statsByName.getOrDefault("special-attack", 0),
                statsByName.getOrDefault("special-defense", 0),
                statsByName.getOrDefault("speed", 0),
                spriteUrl
        );
    }

    /**
     * Resuelve la URL del sprite del Pokémon con múltiples estrategias de
     * fallback. Prioridad: official artwork > front default > URL construida
     * por ID.
     *
     * @param pokemonDetail DTO con información de sprites
     * @param pokemonId ID del Pokémon (usado para fallback)
     * @return URL válida del sprite del Pokémon
     */
    private String resolveSpriteUrl(PokemonDetailDTO pokemonDetail, int pokemonId) {
        // Intentar obtener artwork oficial
        if (pokemonDetail.sprites() != null
                && pokemonDetail.sprites().other() != null
                && pokemonDetail.sprites().other().officialArtwork() != null
                && pokemonDetail.sprites().other().officialArtwork().frontDefault() != null) {
            return pokemonDetail.sprites().other().officialArtwork().frontDefault();
        }

        // Fallback a sprite frontal por defecto
        if (pokemonDetail.sprites() != null && pokemonDetail.sprites().frontDefault() != null) {
            return pokemonDetail.sprites().frontDefault();
        }

        // Fallback final: construir URL usando ID de PokeAPI
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemonId + ".png";
    }

    /**
     * Ordena una lista de Pokémon según el criterio especificado. Soporta
     * ordenamiento por ID y nombre en ambos sentidos.
     *
     * @param pokemonList Lista de Pokémon a ordenar
     * @param sortCriteria Criterio de ordenamiento (id_asc, id_desc, name_asc,
     * name_desc)
     * @return Lista ordenada según el criterio
     */
    private List<PokemonVista> sortPokes(List<PokemonVista> pokemonList, String sortCriteria) {
        List<PokemonVista> sortedList = new ArrayList<>(pokemonList);

        // Definir comparadores
        Comparator<PokemonVista> byIdComparator = Comparator.comparingInt(PokemonVista::id);
        Comparator<PokemonVista> byNameComparator = Comparator.comparing(
                p -> Optional.ofNullable(p.name()).orElse("")
        );

        // Aplicar ordenamiento según criterio
        return switch (sortCriteria) {
            case "id_desc" ->
                sortedList.stream().sorted(byIdComparator.reversed()).toList();
            case "name_asc" ->
                sortedList.stream().sorted(byNameComparator).toList();
            case "name_desc" ->
                sortedList.stream().sorted(byNameComparator.reversed()).toList();
            case "id_asc" ->
                sortedList.stream().sorted(byIdComparator).toList();
            default ->
                sortedList.stream().sorted(byIdComparator).toList();  
            };
    }
}
