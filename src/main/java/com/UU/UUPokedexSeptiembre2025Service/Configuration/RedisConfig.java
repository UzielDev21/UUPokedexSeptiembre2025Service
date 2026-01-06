package com.UU.UUPokedexSeptiembre2025Service.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    
/*
    *CONEXIÓN A REDIS
--------------------------------------
    *Nos permite el almacenamiento de token JWT en una bd NoSQL
    *Mejora el rendimiento y no generamos cuellos de botella en la BD principal
--------------------------------------    
*/
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration("localhost", 6379);
        
        return new LettuceConnectionFactory(redisConfig);
    }
    
/*
    *SERIALZIACIÓN
--------------------------------------
    *Configarción de RedisTemplate para generar la serialización de de nuestros datos a redis
    *Nos permite enviar y recibir datos de Redis
--------------------------------------    
*/
    @Bean
    public StringRedisTemplate stringredisTemplate(RedisConnectionFactory connectionFactory){
        
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }
    
}
