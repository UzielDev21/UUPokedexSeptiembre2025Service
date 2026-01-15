package com.UU.UUPokedexSeptiembre2025Service.Configuration;

import jakarta.jms.Queue;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@EnableJms
public class EmbeddedArtemisConfiguration {
    
    @Bean
    public Queue verificationEmailQueue(){
        return new ActiveMQQueue("queue.email.verification");
    }
    
    @Bean
    public Queue passwordEmailQueue(){
        return new ActiveMQQueue("queue.email.passwordreset");
    }
    
}
