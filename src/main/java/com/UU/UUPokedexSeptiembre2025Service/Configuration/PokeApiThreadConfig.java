package com.UU.UUPokedexSeptiembre2025Service.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Configuración de pool de hilos para operaciones concurrentes en PokeAPI.
 * 
 * Define un ExecutorService con 8 hilos fijos que se utiliza para:
 * - Obtener detalles de múltiples Pokémon en paralelo
 * - Limitar la concurrencia máxima hacia la API (evitar saturación)
 * - Mejorar el rendimiento de búsquedas y filtrados
 * 
 * El pool se cierra automáticamente cuando la aplicación Spring termina.
 */
@Configuration
public class PokeApiThreadConfig {

    /**
     * Crea un ExecutorService con pool fijo de 8 hilos.
     * 
     * Este bean se inyecta automáticamente en PokedexService y se utiliza
     * en el método mapLimit() para ejecutar tareas de forma paralela.
     * 
     * Cantidad de hilos (8):
     * - Ofrece buen balance entre velocidad y consumo de recursos
     * - Coincide con el límite de concurrencia en mapLimit()
     * - Previene sobrecarga de la API PokeAPI
     * 
     * @return ExecutorService con 8 hilos permanentes
     */
    @Bean(destroyMethod = "shutdown")
    public ExecutorService pokeExecutor() {
        // Crear pool fijo de 8 hilos para operaciones concurrentes
        // destroyMethod="shutdown" cierra los hilos al terminar la app
        return Executors.newFixedThreadPool(8);
    }
}
