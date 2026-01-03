package com.UU.UUPokedexSeptiembre2025Service.DAO;

import com.UU.UUPokedexSeptiembre2025Service.JPA.UsuariosJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepository extends JpaRepository<UsuariosJPA, Integer>{
    
    UsuariosJPA findByUserName(String userName);
    
    UsuariosJPA findByEmail(String Email);
    
}
