package com.UU.UUPokedexSeptiembre2025Service.Producer;

import com.UU.UUPokedexSeptiembre2025Service.DTO.VerificationEmailMessage;
import jakarta.jms.Queue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class VerificationEmailProducer {

    private final JmsTemplate jmsTemplate;
    private final Queue verificationEmailQueue;

    public VerificationEmailProducer(JmsTemplate jmsTemplate,
            Queue verificationEmailQueue) {
        this.jmsTemplate = jmsTemplate;
        this.verificationEmailQueue = verificationEmailQueue;
    }

    public void senderVerificationEmail(String email, String nombre, String token) {

        VerificationEmailMessage message = new VerificationEmailMessage(email, nombre, token);

        jmsTemplate.convertAndSend(verificationEmailQueue, message);
    }
}
