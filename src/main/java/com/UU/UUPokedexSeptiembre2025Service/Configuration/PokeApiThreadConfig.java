package com.UU.UUPokedexSeptiembre2025Service.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class PokeApiThreadConfig {

    @Bean(destroyMethod = "shutdown")
    public ExecutorService pokeExecutor() {
        return Executors.newFixedThreadPool(8);
    }
}
