package com.UU.UUPokedexSeptiembre2025Service.JPA;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "USUARIO_ROL")
public class Usuario_RolJPA {
    
    @ManyToMany(mappedBy = "UsuariosJPA", cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "No puede ir el valor vacio")
    @Positive(message = "No pueden haber id's de usuarios negativos")
    public int user_Id;
    
    @ManyToMany(mappedBy = "RolesJPA", cascade = CascadeType.ALL)
    @Column(name = "role_id", nullable = false)
    @NotNull(message = "No puede ir el valor vacio")
    @Positive(message = "No pueden haber id's de roles negativos")
    public int role_Id;

}
