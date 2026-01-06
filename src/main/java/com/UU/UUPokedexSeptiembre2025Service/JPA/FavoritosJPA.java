package com.UU.UUPokedexSeptiembre2025Service.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "FAVORITOS")
public class FavoritosJPA {

//-----------------------------------Atributos-----------------------------------//
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favoritos_id", nullable = false)
    @NotNull(message = "No puedes agregar valores nulos")
    private int favoritos_Id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "No puedes agregar valores nulos")
    public UsuariosJPA usuariosJPA;
    
    @Column(name = "pokemon_id", nullable = false)
    @NotNull
    @Positive
    private Integer pokemon_Id;
    
//-----------------------------------Constructores-----------------------------------//
    
    public FavoritosJPA(){
    }
    
    public FavoritosJPA(int favoritos_Id, UsuariosJPA usuariosJPA, Integer pokemon_Id){
        this.favoritos_Id = favoritos_Id;
        this.usuariosJPA = usuariosJPA;
        this.pokemon_Id = pokemon_Id;
    }
    
//-----------------------------------Setter y Getter-----------------------------------//

    public int getFavoritos_Id() {
        return favoritos_Id;
    }

    public void setFavoritos_Id(int favoritos_Id) {
        this.favoritos_Id = favoritos_Id;
    }

    public UsuariosJPA getUsuariosJPA() {
        return usuariosJPA;
    }

    public void setUsuariosJPA(UsuariosJPA usuariosJPA) {
        this.usuariosJPA = usuariosJPA;
    }

    public Integer getPokemon_Id() {
        return pokemon_Id;
    }

    public void setPokemon_Id(Integer pokemon_Id) {
        this.pokemon_Id = pokemon_Id;
    }
    
}
