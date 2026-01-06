package com.UU.UUPokedexSeptiembre2025Service.Service;

import com.UU.UUPokedexSeptiembre2025Service.DAO.IUsuarioRepository;
import com.UU.UUPokedexSeptiembre2025Service.JPA.UsuariosJPA;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsJPAService implements UserDetailsService{
    
    private final IUsuarioRepository iUsuarioRepository;
    
    public UserDetailsJPAService(IUsuarioRepository iUsuarioRepository){
        this.iUsuarioRepository = iUsuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        UsuariosJPA usuario = iUsuarioRepository.findByUserName(username);
        
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado" + username);
        }
        
        int userActive = usuario.getActive();
        boolean isDisable = (userActive == 0);
        
        return User.withUsername(usuario.getUserName())
                .password(usuario.getPassword_Hash())
                .roles(usuario.rolJPA.getRole_Name())
                .disabled(isDisable)
                .build();
    }
    
}
