package com.UU.UUPokedexSeptiembre2025Service.Service;

import com.UU.UUPokedexSeptiembre2025Service.DAO.UsuarioJPADAOImplementation;
import com.UU.UUPokedexSeptiembre2025Service.DTO.UsuarioRegisterDTO;
import com.UU.UUPokedexSeptiembre2025Service.JPA.Result;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioJPADAOImplementation usuarioJPADAOImplementation;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Result registrarUsuario(UsuarioRegisterDTO usuarioDTO) {

        Result result = new Result();

        try {
            
            String encryptedPassword = passwordEncoder.encode(usuarioDTO.getPassword_Hash());
            usuarioDTO.setPassword_Hash(encryptedPassword);
            result = usuarioJPADAOImplementation.AddJPA(usuarioDTO);
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

}
