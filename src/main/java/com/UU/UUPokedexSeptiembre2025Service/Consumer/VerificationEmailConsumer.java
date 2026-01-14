package com.UU.UUPokedexSeptiembre2025Service.Consumer;

import com.UU.UUPokedexSeptiembre2025Service.DTO.VerificationEmailMessage;
import com.UU.UUPokedexSeptiembre2025Service.Service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class VerificationEmailConsumer {
    
    private final EmailService emailService;
    
    public VerificationEmailConsumer(EmailService emailService){
        this.emailService = emailService;
    }

    @JmsListener(destination = "queue.email.verification")
    public void receiveVerificationEmail(VerificationEmailMessage message){
        
        System.out.println("[JMS] mensaje recibido para: " + message.getEmail());
        
        
        try {
            emailService.sendVerificationEmail(
                    message.getEmail(), 
                    message.getNombre(), 
                    message.getTokenEmail());
            System.out.println("[MAIL] correo enviado correctamente a: " + message.getEmail());
            
        } catch (MessagingException ex) {
            System.err.println("[MAIL ERROR] Error, enviando correo a " + message.getEmail() + " -> " + ex.getLocalizedMessage());
        }
    }
    
}
