package com.UU.UUPokedexSeptiembre2025Service.JPA;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USUARIOS")
public class UsuariosJPA {

//-----------------------------------Atributos-----------------------------------//
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    @NotNull(message = "No puedes insertar Null")
    private int user_Id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    @NotBlank(message = "No puede ir el username vacio")
    @Size(min = 6, max = 15, message = "El Username no puede ser menor a 6 digitos ni mayor a 15 digitos")
    private String userName;

    @Column(name = "nombre", nullable = false, length = 50)
    @NotBlank(message = "No puede ir el nombre vacio")
    private String nombre;

    @Column(name = "apellidomaterno", nullable = false, length = 50)
    @NotBlank(message = "No puede ir el apellido paterno vacio")
    private String apellidoPaterno;

    @Column(name = "apellidopaterno", nullable = false, length = 50)
    @NotBlank(message = "No puede ir el apellido materno vacio")
    private String apellidoMaterno;

    @Column(name = "sexo", nullable = false, length = 50)
    @NotBlank(message = "No puede ir el sexo vacio")
    private String sexo;

    @Column(name = "email", nullable = false, unique = true, length = 120)
    @NotNull(message = "Debes de ingresar un correo obligatoriamente")
    @NotBlank
    @Email(message = "Ingresa un correo valido")
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    @NotNull
    @NotBlank
    private String password_Hash;

    @Column(name = "active", nullable = false, length = 1)
    @NotNull
    private int active;

    @Column(name = "is_verified", nullable = false, length = 1)
    @NotNull
    private int is_Verified;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    @NotNull(message = "No puede ir el id del rol vacio")
    public RolesJPA rolJPA;

    @OneToMany(mappedBy = "usuariosJPA", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    public List<FavoritosJPA> favoritosJPA = new ArrayList<>();

    /*
--------------------------------------
    *Este metodo inserta los datos de Active y Is_verified
    *Este metodo es invocado automaticamente para establecer valores por default
    *Active = 1
    *Is_verified = 0
--------------------------------------    
     */
    @PrePersist
    private void PrePersist() {
        active = 1;
        is_Verified = 0;
    }

//-----------------------------------Constructores-----------------------------------//
    public UsuariosJPA() {
    }

    public UsuariosJPA(int user_Id, String userName, String email, String password_Hash, int active, int is_Verified) {
        this.user_Id = user_Id;
        this.userName = userName;
        this.email = email;
        this.password_Hash = password_Hash;
        this.active = active;
        this.is_Verified = is_Verified;
    }

//-----------------------------------Constructores-----------------------------------//
    public int getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(int user_Id) {
        this.user_Id = user_Id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_Hash() {
        return password_Hash;
    }

    public void setPassword_Hash(String password_Hash) {
        this.password_Hash = password_Hash;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getIs_Verified() {
        return is_Verified;
    }

    public void setIs_Verified(int is_Verified) {
        this.is_Verified = is_Verified;
    }

    public RolesJPA getRolJPA() {
        return rolJPA;
    }

    public void setRolJPA(RolesJPA rolJPA) {
        this.rolJPA = rolJPA;
    }

    public List<FavoritosJPA> getFavoritosJPA() {
        return favoritosJPA;
    }

    public void setFavoritosJPA(List<FavoritosJPA> favoritosJPA) {
        this.favoritosJPA = favoritosJPA;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    
    

}
