    package com.UU.UUPokedexSeptiembre2025Service.Service;

    import com.UU.UUPokedexSeptiembre2025Service.Configuration.PokeApi;
    import com.UU.UUPokedexSeptiembre2025Service.DTO.PokePageDTO;
    import com.UU.UUPokedexSeptiembre2025Service.DTO.PokeRefDTO;
    import com.UU.UUPokedexSeptiembre2025Service.DTO.PokedexResponse;
    import com.UU.UUPokedexSeptiembre2025Service.DTO.PokemonDetailDTO;
    import com.UU.UUPokedexSeptiembre2025Service.DTO.PokemonStatSlotDTO;
    import com.UU.UUPokedexSeptiembre2025Service.DTO.PokemonVm;
    import com.UU.UUPokedexSeptiembre2025Service.DTO.TypeDetailDTO;
    import com.UU.UUPokedexSeptiembre2025Service.Util.SynchronizedLruCache;
    import org.springframework.stereotype.Service;

    import java.util.*;
    import java.util.concurrent.*;
    import java.util.function.Function;

    @Service
    public class PokedexService {

        private final PokeApi api;
        private final ExecutorService executor;

        // === caches como tu JS
        private final SynchronizedLruCache<String, PokemonDetailDTO> detailsCache = new SynchronizedLruCache<>(300);
        private final SynchronizedLruCache<String, TypeDetailDTO> typeCache = new SynchronizedLruCache<>(80);
        private final SynchronizedLruCache<String, PokedexResponse> pageCache = new SynchronizedLruCache<>(80);

        public PokedexService(PokeApi api, ExecutorService pokeApiExecutor) {
            this.api = api;
            this.executor = pokeApiExecutor;
        }

        // === método “main” igual al load() del JS
        public PokedexResponse load(String q, String type, int limit, int offset, String sort) {
            q = (q == null ? "" : q.trim().toLowerCase());
            type = (type == null ? "" : type.trim().toLowerCase());
            sort = (sort == null ? "id_asc" : sort.trim().toLowerCase());

            if (!q.isBlank()) {
                // search fuerza offset=0 en tu JS
                PokemonDetailDTO d = getPokemonDetailCached(q);
                return new PokedexResponse(sortPokes(List.of(toVm(d)), sort), 1, false, false);
            }

            if (!type.isBlank()) {
                return loadType(type, limit, offset, sort);
            }

            return loadList(limit, offset, sort);
        }

        private PokedexResponse loadList(int limit, int offset, String sort) {
            String cacheKey = "list|limit=" + limit + "|offset=" + offset + "|sort=" + sort;
            if (pageCache.containsKey(cacheKey)) return pageCache.get(cacheKey);

            PokePageDTO page = api.getPokemonPage(limit, offset);
            List<String> names = page.results() == null ? List.of() :
                    page.results().stream().map(PokeRefDTO::name).toList();

            List<PokemonDetailDTO> details = mapLimit(names, 8, this::getPokemonDetailCached);
            List<PokemonVm> vms = sortPokes(details.stream().map(this::toVm).toList(), sort);

            PokedexResponse res = new PokedexResponse(
                    vms,
                    page.count(),
                    page.next() != null && !page.next().isBlank(),
                    page.previous() != null && !page.previous().isBlank()
            );

            pageCache.put(cacheKey, res);
            return res;
        }

        private PokedexResponse loadType(String type, int limit, int offset, String sort) {
            String cacheKey = "type|" + type + "|limit=" + limit + "|offset=" + offset + "|sort=" + sort;
            if (pageCache.containsKey(cacheKey)) return pageCache.get(cacheKey);

            TypeDetailDTO td = getTypeDetailCached(type);

            List<String> all = td.pokemon() == null ? List.of() :
                    td.pokemon().stream()
                            .map(x -> x.pokemon() != null ? x.pokemon().name() : null)
                            .filter(Objects::nonNull)
                            .toList();

            int total = all.size();
            int from = Math.min(offset, total);
            int to = Math.min(offset + limit, total);

            List<String> slice = all.subList(from, to);

            List<PokemonDetailDTO> details = mapLimit(slice, 8, this::getPokemonDetailCached);
            List<PokemonVm> vms = sortPokes(details.stream().map(this::toVm).toList(), sort);

            PokedexResponse res = new PokedexResponse(
                    vms,
                    total,
                    offset + limit < total,
                    offset > 0
            );

            pageCache.put(cacheKey, res);
            return res;
        }

        private <T, R> List<R> mapLimit(List<T> items, int limit, Function<T, R> fn) {
            if (items == null || items.isEmpty()) return List.of();

            Semaphore sem = new Semaphore(Math.max(1, limit));

            List<CompletableFuture<R>> futures = items.stream()
                    .map(item -> CompletableFuture.supplyAsync(() -> {
                        try {
                            sem.acquire();
                            return fn.apply(item);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            throw new RuntimeException(e);
                        } finally {
                            sem.release();
                        }
                    }, executor))
                    .toList();

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            List<R> out = new ArrayList<>(items.size());
            for (CompletableFuture<R> f : futures) out.add(f.join());
            return out;
        }

        // === caches como tu JS
        private PokemonDetailDTO getPokemonDetailCached(String key) {
            String k = key.toLowerCase();
            if (detailsCache.containsKey(k)) return detailsCache.get(k);

            PokemonDetailDTO d = api.getPokemonDetail(k);
            detailsCache.put(k, d);
            return d;
        }

        private TypeDetailDTO getTypeDetailCached(String type) {
            String k = type.toLowerCase();
            if (typeCache.containsKey(k)) return typeCache.get(k);

            TypeDetailDTO d = api.getTypeDetail(k);
            typeCache.put(k, d);
            return d;
        }

        // === toVM(detail) como tu JS
        private PokemonVm toVm(PokemonDetailDTO detail) {
            int id = detail.id();
            String name = detail.name();

            List<String> types = detail.types() == null ? List.of() :
                    detail.types().stream()
                            .map(x -> x.type() != null ? x.type().name() : null)
                            .filter(Objects::nonNull)
                            .toList();

            Map<String, Integer> stats = new HashMap<>();
            if (detail.stats() != null) {
                for (PokemonStatSlotDTO s : detail.stats()) {
                    if (s.stat() != null && s.stat().name() != null) {
                        stats.put(s.stat().name(), s.baseStat());
                    }
                }
            }

            String sprite =
                    (detail.sprites() != null
                            && detail.sprites().other() != null
                            && detail.sprites().other().officialArtwork() != null
                            && detail.sprites().other().officialArtwork().frontDefault() != null)
                            ? detail.sprites().other().officialArtwork().frontDefault()
                            : (detail.sprites() != null && detail.sprites().frontDefault() != null)
                            ? detail.sprites().frontDefault()
                            : "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";

            return new PokemonVm(
                    id,
                    name,
                    types,
                    stats.get("hp"),
                    stats.get("attack"),
                    stats.get("defense"),
                    stats.get("special-attack"),
                    stats.get("special-defense"),
                    stats.get("speed"),
                    sprite
            );
        }

        // === sort igual al JS
        private List<PokemonVm> sortPokes(List<PokemonVm> pokes, String sort) {
            List<PokemonVm> arr = new ArrayList<>(pokes);

            Comparator<PokemonVm> byId = Comparator.comparingInt(PokemonVm::id);
            Comparator<PokemonVm> byName = Comparator.comparing(p -> Optional.ofNullable(p.name()).orElse(""));

            return switch (sort) {
                case "id_desc" -> arr.stream().sorted(byId.reversed()).toList();
                case "name_asc" -> arr.stream().sorted(byName).toList();
                case "name_desc" -> arr.stream().sorted(byName.reversed()).toList();
                case "id_asc" -> arr.stream().sorted(byId).toList();
                default -> arr.stream().sorted(byId).toList();
            };
        }
    }
