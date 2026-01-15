package com.UU.UUPokedexSeptiembre2025Service.DTO;

import java.io.Serializable;

public class VerificationEmailMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String email;
    private String nombre;
    private String tokenEmail;
    
    public VerificationEmailMessage(){
    }
    
    public VerificationEmailMessage(String email, String nombre, String tokenEmail){
        this.email = email;
        this.nombre = nombre;
        this.tokenEmail = tokenEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTokenEmail() {
        return tokenEmail;
    }

    public void setTokenEmail(String tokenEmail) {
        this.tokenEmail = tokenEmail;
    }
}
