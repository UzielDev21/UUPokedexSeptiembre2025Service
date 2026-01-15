package com.UU.UUPokedexSeptiembre2025Service.DAO;

import com.UU.UUPokedexSeptiembre2025Service.DTO.UsuarioRegisterDTO;
import com.UU.UUPokedexSeptiembre2025Service.JPA.Result;
import com.UU.UUPokedexSeptiembre2025Service.JPA.UsuariosJPA;

public interface IUsuarioJPA {

    Result GetAll();
    
    Result GetByIdUser(int user_Id);
    
    Result VerifyUser(int user_Id);
    
    Result AddJPA(UsuarioRegisterDTO usuariosJPA);
    
    Result Update(UsuariosJPA usuariosJPA);
    
    Result Delete(int user_id);
    
}
