package com.UU.UUPokedexSeptiembre2025Service.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ROLES")
public class RolesJPA {
    
//-----------------------------------ATRIBUTOS-----------------------------------//
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    @Positive(message = "No puedes poner Id's negativas")
    private int role_Id;

    @Column(name = "role_name", nullable = false, unique = true, length = 30)
    @NotBlank(message = "No puedes introducir un rol vacio")
    @Size(min = 2, max = 30, message = "No puedes introducir un rol de solo 2 caracteres")
    private String role_Name;
    
//-----------------------------------CONSTRUCTORES-----------------------------------//  
    
    public RolesJPA(){
    }
    
    public RolesJPA(int role_Id, String role_Name){
        this.role_Id = role_Id;
        this.role_Name = role_Name;
    }
    
//-----------------------------------SETTERS Y GETTERS-----------------------------------//    

    public int getRole_Id() {
        return role_Id;
    }

    public void setRole_Id(int role_Id) {
        this.role_Id = role_Id;
    }

    public String getRole_Name() {
        return role_Name;
    }

    public void setRole_Name(String role_Name) {
        this.role_Name = role_Name;
    }
 
    
    
}
